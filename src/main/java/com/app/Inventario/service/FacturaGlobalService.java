package com.app.Inventario.service;

import com.app.Inventario.mapper.FacturaGlobalMapper;
import com.app.Inventario.model.dto.response.ConsolidacionItemResponseDTO;
import com.app.Inventario.model.dto.request.FacturaGlobalRequestDTO;
import com.app.Inventario.model.dto.response.FacturaGlobalResponseDTO;
import com.app.Inventario.model.entity.*;
import com.app.Inventario.model.enums.EstadoFacturaGlobal;
import com.app.Inventario.model.enums.EstadoSolicitud;
import com.app.Inventario.repository.FacturaGlobalRepository;
import com.app.Inventario.repository.SolicitudGilRepository;
import com.app.Inventario.repository.SolicitudItemsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacturaGlobalService {

    private final FacturaGlobalRepository facturaGlobalRepository;
    private final SolicitudGilRepository solicitudGilRepository;
    private final SolicitudItemsRepository solicitudItemRepository;
    private final FacturaGlobalMapper facturaGlobalMapper;

    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    private static final String PREFIJO_FACTURA = "FG-";

    @Transactional
    public FacturaGlobalResponseDTO generarFactura(FacturaGlobalRequestDTO request) {

        List<Long> gilF014Ids = request.getGilF014Ids();

        validarDuplicados(gilF014Ids);
        List<SolicitudGil> solicitudesAConsolidar = obtenerSolicitudesValidas(gilF014Ids);

        List<ConsolidacionItemResponseDTO> itemsConsolidadosDTO = consolidarBienes(solicitudesAConsolidar);

        FacturaGlobal nuevaFactura = calcularTotales(itemsConsolidadosDTO);
        nuevaFactura.setNumeroConsecutivo(generarSiguienteNumeroFactura());
        nuevaFactura.setFechaGeneracion(LocalDateTime.now());
        nuevaFactura.setEstado(EstadoFacturaGlobal.GENERADA);

        List<FacturaGlobalDetalle> detalles = itemsConsolidadosDTO.stream()
                .map(dto -> mapToDetalleEntity(dto, nuevaFactura))
                .collect(Collectors.toList());

        nuevaFactura.setDetalles(detalles);

        FacturaGlobal facturaGuardada = facturaGlobalRepository.save(nuevaFactura);

        asociarFacturaAGIL(facturaGuardada, solicitudesAConsolidar);

        return facturaGlobalMapper.toResponseDto(
                facturaGuardada,
                itemsConsolidadosDTO,
                solicitudesAConsolidar
        );
    }

    private FacturaGlobalDetalle mapToDetalleEntity(ConsolidacionItemResponseDTO dto, FacturaGlobal factura) {
        FacturaGlobalDetalle detalle = new FacturaGlobalDetalle();
        detalle.setFacturaGlobal(factura);
        detalle.setCodigoProducto(dto.getCodigoProducto());
        detalle.setDescripcion(dto.getDescripcion());
        detalle.setCantidad(dto.getCantidad());
        detalle.setValorUnitarioSinIva(dto.getValorUnitarioAntesIva());
        detalle.setIvaPorcentaje(dto.getIvaPorcentaje());
        detalle.setTotalSinIva(dto.getTotalSinIva());
        detalle.setValorIva(dto.getValorIva());
        detalle.setTotalConIva(dto.getTotalConIva());
        return detalle;
    }


    private void validarDuplicados(List<Long> gilF014Ids) {
        List<SolicitudGil> duplicados = solicitudGilRepository.findByIdInAndFacturaGlobalIsNotNull(gilF014Ids);
        if (!duplicados.isEmpty()) {
            String codigos = duplicados.stream().map(SolicitudGil::getCodigo).collect(Collectors.joining(", "));
            throw new RuntimeException("Error: El GIL-F-014 con código(s): " + codigos + " ya está en una factura consolidada.");
        }
    }

    private List<SolicitudGil> obtenerSolicitudesValidas(List<Long> gilF014Ids) {
        List<SolicitudGil> solicitudes = solicitudGilRepository.findByIdInAndEstadoAndFacturaGlobalIsNull(
                gilF014Ids, EstadoSolicitud.VALIDADA);

        if (solicitudes.size() != gilF014Ids.size()) {
            throw new RuntimeException("Error: No todas las solicitudes seleccionadas existen o están en estado 'VALIDADA'.");
        }
        return solicitudes;
    }

    private List<ConsolidacionItemResponseDTO> consolidarBienes(List<SolicitudGil> solicitudes) {
        List<Long> solicitudIds = solicitudes.stream().map(SolicitudGil::getId).collect(Collectors.toList());

        List<SolicitudItems> items = solicitudItemRepository.findBySolicitudGil_IdIn(solicitudIds);

        return items.stream()
                .collect(Collectors.groupingBy(
                        item -> item.getBien().getCodigo(),
                        Collectors.reducing(
                                null,
                                this::mapToConsolidacionItem,
                                this::combineConsolidacionItems
                        )
                ))
                .values().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // --- CORRECCIÓN PRINCIPAL AQUÍ ---
    private ConsolidacionItemResponseDTO mapToConsolidacionItem(SolicitudItems item) {
        Bien bien = item.getBien();

        // 1. Obtener porcentaje desde la entidad Impuesto de forma segura
        BigDecimal porcentajeIva = BigDecimal.ZERO;

        // Asumimos que la entidad Bien tiene el método getImpuesto()
        if (bien.getImpuesto() != null && bien.getImpuesto().getPorcentaje() != null) {
            porcentajeIva = bien.getImpuesto().getPorcentaje();
        }

        // 2. Cálculos matemáticos
        BigDecimal totalSinIva = bien.getValorUnitario()
                .multiply(item.getCantidad())
                .setScale(SCALE, ROUNDING_MODE);

        BigDecimal ivaAmount = totalSinIva
                .multiply(porcentajeIva)
                .divide(new BigDecimal("100"), SCALE, ROUNDING_MODE);

        // 3. Construir DTO
        return ConsolidacionItemResponseDTO.builder()
                .codigoProducto(bien.getCodigo())
                .descripcion(bien.getNombre())
                .valorUnitarioAntesIva(bien.getValorUnitario().setScale(SCALE, ROUNDING_MODE))
                // Usamos la variable local calculada 'porcentajeIva'
                .ivaPorcentaje(porcentajeIva.setScale(SCALE, ROUNDING_MODE))
                .cantidad(item.getCantidad().setScale(SCALE, ROUNDING_MODE))
                .totalSinIva(totalSinIva)
                .valorIva(ivaAmount)
                .totalConIva(totalSinIva.add(ivaAmount))
                .build();
    }

    private ConsolidacionItemResponseDTO combineConsolidacionItems(ConsolidacionItemResponseDTO dto1, ConsolidacionItemResponseDTO dto2) {
        if (dto1 == null) return dto2;
        if (dto2 == null) return dto1;

        return ConsolidacionItemResponseDTO.builder()
                .codigoProducto(dto1.getCodigoProducto())
                .descripcion(dto1.getDescripcion())
                .valorUnitarioAntesIva(dto1.getValorUnitarioAntesIva())
                .ivaPorcentaje(dto1.getIvaPorcentaje())
                .cantidad(dto1.getCantidad().add(dto2.getCantidad()).setScale(SCALE, ROUNDING_MODE))
                .totalSinIva(dto1.getTotalSinIva().add(dto2.getTotalSinIva()).setScale(SCALE, ROUNDING_MODE))
                .valorIva(dto1.getValorIva().add(dto2.getValorIva()).setScale(SCALE, ROUNDING_MODE))
                .totalConIva(dto1.getTotalConIva().add(dto2.getTotalConIva()).setScale(SCALE, ROUNDING_MODE))
                .build();
    }

    private FacturaGlobal calcularTotales(List<ConsolidacionItemResponseDTO> items) {
        BigDecimal totalGeneral = items.stream()
                .map(ConsolidacionItemResponseDTO::getTotalConIva)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(SCALE, ROUNDING_MODE);

        FacturaGlobal factura = new FacturaGlobal();
        factura.setTotalFinal(totalGeneral);
        return factura;
    }

    private String generarSiguienteNumeroFactura() {
        Optional<FacturaGlobal> ultimaFactura = facturaGlobalRepository.findTopByOrderByNumeroConsecutivoDesc();

        if (ultimaFactura.isEmpty()) {
            return PREFIJO_FACTURA + String.format("%05d", 1);
        }

        String ultimoNumero = ultimaFactura.get().getNumeroConsecutivo();
        try {
            int consecutivo = Integer.parseInt(ultimoNumero.substring(PREFIJO_FACTURA.length()));
            return PREFIJO_FACTURA + String.format("%05d", consecutivo + 1);
        } catch (Exception e) {
            throw new RuntimeException("Error (RF-5.4.3): Fallo al generar el número de factura consecutivo.", e);
        }
    }

    private void asociarFacturaAGIL(FacturaGlobal factura, List<SolicitudGil> solicitudes) {
        solicitudes.forEach(gil -> gil.setFacturaGlobal(factura));
        solicitudGilRepository.saveAll(solicitudes);
    }

    @Transactional(readOnly = true)
    public List<FacturaGlobalResponseDTO> listarTodas() {
        return facturaGlobalRepository.findAll().stream()
                .map(factura -> {
                    List<SolicitudGil> solicitudesAsociadas = solicitudGilRepository.findByFacturaGlobal(factura);

                    return FacturaGlobalResponseDTO.builder()
                            .id(factura.getId())
                            .numeroFactura(factura.getNumeroConsecutivo())
                            .fechaGeneracion(factura.getFechaGeneracion())
                            .totalGeneral(factura.getTotalFinal())
                            .estado(factura.getEstado())
                            .gilF014Asociados(solicitudesAsociadas.stream().map(SolicitudGil::getId).collect(Collectors.toList()))
                            .build();
                })
                .collect(Collectors.toList());
    }

    public byte[] exportar(Long id, String formato) {
        throw new UnsupportedOperationException("La funcionalidad de exportación (RF-5.4.6) aún no está implementada.");
    }
}