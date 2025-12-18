package com.app.Inventario.service;

import com.app.Inventario.mapper.CuentadanteMapper;
import com.app.Inventario.mapper.SolicitudGilMapper;
import com.app.Inventario.model.dto.request.SolicitudGilRequestDTO;
import com.app.Inventario.model.dto.response.SolicitudGilResponseDTO;
import com.app.Inventario.model.entity.PreFactura;
import com.app.Inventario.model.entity.SolicitudGil;
import com.app.Inventario.model.entity.SolicitudItems;
import com.app.Inventario.model.entityMaestras.Cuentadante;
import com.app.Inventario.model.enums.EstadoPreFactura;
import com.app.Inventario.model.enums.EstadoSolicitud;
import com.app.Inventario.model.enums.TipoCuentadante;
import com.app.Inventario.repository.SolicitudGilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SolicitudGilService {

    private final SolicitudGilRepository solicitudGilRepository;
    private final SolicitudGilMapper solicitudGilMapper;
    private final PreFacturaService preFacturaService;
    private final CuentadanteMapper cuentadanteMapper;

    @Transactional
    public SolicitudGilResponseDTO crearSolicitud(SolicitudGilRequestDTO dto) {
        PreFactura preFactura = preFacturaService.obtenerEntidadPorId(dto.getPreFacturaId());

        if (preFactura.getEstado() != EstadoPreFactura.VALIDADA) {
            throw new RuntimeException("La PreFactura [" + dto.getPreFacturaId() + "] debe estar VALIDADA.");
        }

        if (dto.getTipoCuentadante() == TipoCuentadante.UNIPERSONAL
                && dto.getCuentadantes() != null
                && dto.getCuentadantes().size() > 1) {
            throw new RuntimeException("Solo se permite un cuentadante cuando el tipo es UNIPERSONAL.");
        }

        SolicitudGil solicitud = solicitudGilMapper.toEntity(dto);
        solicitud.setPreFactura(preFactura);
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);

        // 1. Duplicar y Congelar Items usando Set
        if (preFactura.getDetalles() != null) {
            Set<SolicitudItems> items = preFactura.getDetalles().stream()
                    .map(detalle -> {
                        SolicitudItems item = new SolicitudItems();
                        item.setSolicitudGil(solicitud);
                        item.setBien(detalle.getBien());
                        item.setCantidad(detalle.getCantidad());
                        item.setPrecioCongelado(detalle.getPrecioAdjudicado());
                        item.setTotalLinea(detalle.getTotalLinea());
                        return item;
                    })
                    .collect(Collectors.toSet()); // Cambio a Set
            solicitud.setItems(items);
        }

        // 2. Vincular Cuentadantes usando Set
        if (dto.getCuentadantes() != null && !dto.getCuentadantes().isEmpty()) {
            Set<Cuentadante> cuentadantes = dto.getCuentadantes().stream()
                    .map(cDto -> {
                        Cuentadante c = cuentadanteMapper.toEntity(cDto);
                        c.setSolicitudGil(solicitud);
                        return c;
                    })
                    .collect(Collectors.toSet()); // Cambio a Set
            solicitud.setCuentadantes(cuentadantes);
        }

        SolicitudGil saved = solicitudGilRepository.save(solicitud);
        return solicitudGilMapper.toResponseDto(saved);
    }

    @Transactional(readOnly = true)
    public SolicitudGilResponseDTO obtenerId(Long id) {
        // findFullById debe realizar LEFT JOIN FETCH de items y cuentadantes
        SolicitudGil solicitud = solicitudGilRepository.findFullById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud GIL no encontrada: " + id));

        return solicitudGilMapper.toResponseDto(solicitud);
    }

    @Transactional(readOnly = true)
    public List<SolicitudGilResponseDTO> obtenerSolicitudes() {
        return solicitudGilMapper.toResponseDtos(solicitudGilRepository.findAllFull());
    }

    @Transactional
    public SolicitudGilResponseDTO actualizarSolicitud(Long id, SolicitudGilRequestDTO dto) {
        SolicitudGil solicitud = solicitudGilRepository.findFullById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada: " + id));

        if (solicitud.getEstado() != EstadoSolicitud.PENDIENTE) {
            throw new RuntimeException("Solo se puede modificar una solicitud en estado PENDIENTE.");
        }

        solicitudGilMapper.updateEntityFromDto(solicitud, dto);
        return solicitudGilMapper.toResponseDto(solicitudGilRepository.save(solicitud));
    }

    @Transactional
    public void borrarSolicitud(Long id) {
        SolicitudGil solicitud = solicitudGilRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada: " + id));

        if (solicitud.getEstado() != EstadoSolicitud.PENDIENTE) {
            throw new RuntimeException("Solo se pueden eliminar solicitudes en estado PENDIENTE.");
        }

        solicitudGilRepository.delete(solicitud);
    }

    @Transactional
    public SolicitudGilResponseDTO actualizarEstado(Long id, EstadoSolicitud nuevoEstado) {
        SolicitudGil solicitud = solicitudGilRepository.findFullById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada: " + id));

        if (solicitud.getEstado() == EstadoSolicitud.APROBADA || solicitud.getEstado() == EstadoSolicitud.RECHAZADA) {
            throw new RuntimeException("No se puede modificar una solicitud ya finalizada.");
        }

        solicitud.setEstado(nuevoEstado);
        return solicitudGilMapper.toResponseDto(solicitudGilRepository.save(solicitud));
    }

    @Transactional(readOnly = true)
    public SolicitudGil obtenerEntidadPorId(Long id) {
        return solicitudGilRepository.findFullById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud GIL no encontrada: " + id));
    }
}