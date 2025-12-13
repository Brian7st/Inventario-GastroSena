package com.app.Inventario.service;

import com.app.Inventario.mapper.PreFacturaDetalleMapper;
import com.app.Inventario.mapper.PreFacturaMapper;
import com.app.Inventario.model.dto.PreFacturaDetalleRequestDTO;
import com.app.Inventario.model.dto.PreFacturaRequestDTO;
import com.app.Inventario.model.dto.PreFacturaResponseDTO;
import com.app.Inventario.model.entity.Bien;
import com.app.Inventario.model.entity.PreFactura;
import com.app.Inventario.model.entity.PreFacturaDetalle;
import com.app.Inventario.model.entity.ProgramaFormacion;
import com.app.Inventario.model.enums.EstadoBien;
import com.app.Inventario.model.enums.EstadoPreFactura;
import com.app.Inventario.repository.BienRepository;
import com.app.Inventario.repository.PreFacturaRepository;
import com.app.Inventario.repository.ProgramaFormacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PreFacturaService {


    private final PreFacturaRepository preFacturaRepository;
    private final ProgramaFormacionRepository programaFormacionRepository;
    private final BienRepository bienRepository; // 🏆 AGREGADO: Necesario para validar y actualizar stock


    private final PreFacturaMapper preFacturaMapper;
    private final PreFacturaDetalleMapper preFacturaDetalleMapper; // 🏆 AGREGADO: Necesario para mapear detalles


    @Transactional
    public PreFacturaResponseDTO crearPreFactura(PreFacturaRequestDTO request) {


        if (preFacturaRepository.existsByNumero(request.getNumero())) {
            throw new RuntimeException("Ya existe una PreFactura con el número: " + request.getNumero());
        }

        ProgramaFormacion programa = programaFormacionRepository.findById(request.getProgramaId())
                .orElseThrow(() -> new RuntimeException("Programa de Formación no encontrado con ID: " + request.getProgramaId()));

        PreFactura nuevaPreFactura = preFacturaMapper.toEntity(request, programa);
        nuevaPreFactura.setFecha(LocalDateTime.now());
        nuevaPreFactura.setEstado(EstadoPreFactura.PENDIENTE);
        nuevaPreFactura.setDetalles(new ArrayList<>()); // Inicializa la lista para la relación bidireccional

        PreFactura preFacturaGuardada = preFacturaRepository.save(nuevaPreFactura);

        BigDecimal totalGeneralCalculado = BigDecimal.ZERO;

        for (PreFacturaDetalleRequestDTO detalleRequest : request.getDetalles()) {

            Bien bien = bienRepository.findById(detalleRequest.getBienId())
                    .orElseThrow(() -> new RuntimeException("Bien no encontrado con ID: " + detalleRequest.getBienId() + " en el detalle."));

            if (bien.getStockActual().compareTo(detalleRequest.getCantidad()) < 0) {
                throw new RuntimeException("Stock insuficiente para el bien: " + bien.getNombre() +
                        ". Solicitado: " + detalleRequest.getCantidad() +
                        ", Stock actual: " + bien.getStockActual());
            }

            PreFacturaDetalle detalleEntity = preFacturaDetalleMapper.toEntity(
                    detalleRequest,
                    bien,
                    preFacturaGuardada
            );

            BigDecimal totalLinea = detalleRequest.getCantidad().multiply(detalleRequest.getPrecioAdjudicado());
            totalGeneralCalculado = totalGeneralCalculado.add(totalLinea);

            preFacturaGuardada.getDetalles().add(detalleEntity);


            BigDecimal nuevoStock = bien.getStockActual().subtract(detalleRequest.getCantidad());
            bien.setStockActual(nuevoStock);

            if (nuevoStock.compareTo(bien.getStockMinimo()) < 0 && nuevoStock.compareTo(BigDecimal.ZERO) > 0) {
                bien.setEstado(EstadoBien.BAJO_STOCK);
            } else if (nuevoStock.compareTo(BigDecimal.ZERO) == 0) {
                bien.setEstado(EstadoBien.SIN_STOCK);
            }
            bienRepository.save(bien); // Guardar el Bien con el nuevo stock
        }

        preFacturaGuardada.setTotalPrefactura(totalGeneralCalculado);
        preFacturaRepository.save(preFacturaGuardada);

        return preFacturaMapper.toResponseDtoPreFactura(preFacturaGuardada);
    }

    @Transactional(readOnly = true)
    public List<PreFacturaResponseDTO> listarTodos(){
        return preFacturaRepository
                .findAll().stream()
                .map(preFacturaMapper::toResponseDtoPreFactura)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PreFacturaResponseDTO obtenerPorId(long id){
        PreFactura preFactura = preFacturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PreFactura no encontrada con ID: " + id));

        return preFacturaMapper.toResponseDtoPreFactura(preFactura);
    }

    @Transactional
    public PreFacturaResponseDTO actualizarPreFactura (long id, PreFacturaRequestDTO request){
        PreFactura preFacturaExistente = preFacturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede actualizar, PreFactura no encontrada por ID: " + id));

        if(!preFacturaExistente.getNumero().equals(request.getNumero()) && preFacturaRepository.existsByNumero(request.getNumero())){
            throw new RuntimeException("El número de PreFactura " + request.getNumero() + " ya está en uso por otra prefactura.");
        }

        ProgramaFormacion programa = programaFormacionRepository.findById(request.getProgramaId())
                .orElseThrow(() -> new RuntimeException("Programa de Formación no encontrado con ID: " + request.getProgramaId()));

        preFacturaMapper.updateEntityFromDTO(preFacturaExistente, request, programa);

        PreFactura preFacturaActualizada = preFacturaRepository.save(preFacturaExistente);
        return  preFacturaMapper.toResponseDtoPreFactura(preFacturaActualizada);
    }

    @Transactional
    public void anularPreFactura(long id){
        PreFactura preFactura = preFacturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PreFactura no encontrada ID " + id));

        if(preFactura.getEstado() == EstadoPreFactura.ANULADA){
            throw new RuntimeException("La PreFactura ya se encuentra ANULADA.");
        }

        if (preFactura.getDetalles() != null) {
            for (PreFacturaDetalle detalle : preFactura.getDetalles()) {
                Bien bien = detalle.getBien();
                BigDecimal stockRecuperado = bien.getStockActual().add(detalle.getCantidad());
                bien.setStockActual(stockRecuperado);

                if (stockRecuperado.compareTo(bien.getStockMinimo()) >= 0) {
                    bien.setEstado(EstadoBien.DISPONIBLE);
                }
                bienRepository.save(bien);
            }
        }

        preFactura.setEstado(EstadoPreFactura.ANULADA);
        preFacturaRepository.save(preFactura);
    }
}