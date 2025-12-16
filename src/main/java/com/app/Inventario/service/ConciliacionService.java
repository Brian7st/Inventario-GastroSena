package com.app.Inventario.service;

import com.app.Inventario.mapper.ConciliacionMapper;
import com.app.Inventario.model.dto.response.ConciliacionDetalleResponseDTO;
import com.app.Inventario.model.dto.request.ConciliacionRequestDTO;
import com.app.Inventario.model.dto.response.ConciliacionResponseDTO;
import com.app.Inventario.model.dto.request.ConteoFisicoRequestDTO;
import com.app.Inventario.model.entity.Bien;
import com.app.Inventario.model.entity.Conciliacion;
import com.app.Inventario.model.entity.ConciliacionDetalle;
import com.app.Inventario.model.enums.EstadoConciliacion;
import com.app.Inventario.model.enums.EstadoLinea;
import com.app.Inventario.repository.BienRepository;
import com.app.Inventario.repository.ConciliacionDetalleRepository;
import com.app.Inventario.repository.ConciliacionRepository;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException; 
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConciliacionService {

    private final ConciliacionRepository conciliacionRepository;
    private final ConciliacionDetalleRepository detalleRepository;
    private final BienRepository bienRepository;
    private final ConciliacionMapper conciliacionMapper;

    @Transactional
    public ConciliacionResponseDTO iniciarConciliacion(ConciliacionRequestDTO request) {
        
        Map<Long, Integer> conteoFisicoPorBienId = request.getConteoFisico().stream()
            .collect(Collectors.toMap(
                ConteoFisicoRequestDTO::getBienId,
                dto -> dto.getCantidadReportada().intValue(), 
                (existente, nuevo) -> nuevo 
            ));

        Conciliacion conciliacion = new Conciliacion();
        conciliacion.setFechaCorte(request.getFechaCorte());
        conciliacion.setResponsable(request.getResponsable());
        conciliacion.setEstado(EstadoConciliacion.ABIERTA); 
        conciliacion.setTotalDiferenciaValor(BigDecimal.ZERO);
        
        final Conciliacion conciliacionPersistida = conciliacionRepository.save(conciliacion);
        
        List<Bien> bienes = bienRepository.findAll();
        
        List<ConciliacionDetalle> detalles = bienes.stream()
            .map(bien -> {
                
                Integer conteoSistemaInt = bien.getStockActual().intValue(); 
                
                Integer conteoFisicoReportadoInt = conteoFisicoPorBienId.getOrDefault(
                    bien.getId(), 
                    conteoSistemaInt 
                ); 
                
                BigDecimal conteoSistemaBD = new BigDecimal(conteoSistemaInt);
                BigDecimal conteoFisicoBD = new BigDecimal(conteoFisicoReportadoInt);
                
                BigDecimal diferenciaCantidadBD = conteoFisicoBD.subtract(conteoSistemaBD);
                
                BigDecimal valorUnitario = bien.getValorUnitario();
                BigDecimal diferenciaValor = valorUnitario.multiply(diferenciaCantidadBD.abs());
                
                ConciliacionDetalle detalle = new ConciliacionDetalle();
                
                detalle.setConciliacion(conciliacionPersistida);
                detalle.setBien(bien);
                
                detalle.setConteoSistema(conteoSistemaBD);
                detalle.setConteoFisico(conteoFisicoBD);
                detalle.setDiferenciaCantidad(diferenciaCantidadBD);
                detalle.setValorDiferencia(diferenciaValor);
                
                if (diferenciaCantidadBD.compareTo(BigDecimal.ZERO) == 0) {
                    detalle.setEstadoLinea(EstadoLinea.AJUSTADA); 
                } else {
                    detalle.setEstadoLinea(EstadoLinea.PENDIENTE); 
                }
                
                return detalle;
            })
            .collect(Collectors.toList());

        BigDecimal totalDiferenciaAcumulada = detalles.stream()
            .filter(d -> d.getEstadoLinea() == EstadoLinea.PENDIENTE) 
            .map(ConciliacionDetalle::getValorDiferencia)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        detalleRepository.saveAll(detalles);
        conciliacionPersistida.setDetalles(detalles);
        conciliacionPersistida.setTotalDiferenciaValor(totalDiferenciaAcumulada);
        conciliacionRepository.save(conciliacionPersistida);

        return conciliacionMapper.toResponseDto(conciliacionPersistida);
    }

    @Transactional(readOnly = true)
    public ConciliacionResponseDTO obtenerPorId(Long id) {
        Conciliacion conciliacion = conciliacionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Conciliación no encontrada con ID: " + id));
        
        return conciliacionMapper.toResponseDto(conciliacion);
    }

    @Transactional
    public ConciliacionDetalleResponseDTO justificarDiferencia(Long detalleId, String justificacion) {
        ConciliacionDetalle detalle = detalleRepository.findById(detalleId)
                .orElseThrow(() -> new NoSuchElementException("Detalle de Conciliación no encontrado con ID: " + detalleId));
        
        if (detalle.getDiferenciaCantidad() == null || 
            detalle.getDiferenciaCantidad().compareTo(BigDecimal.ZERO) == 0) {
            
            throw new ValidationException("La línea ya está conciliada (diferencia cero) y no requiere justificación.");
        }
        
        if (detalle.getConciliacion().getEstado() != EstadoConciliacion.ABIERTA) { 
            throw new ValidationException("No se puede justificar el detalle porque la conciliación no está abierta. Estado actual: " + detalle.getConciliacion().getEstado());
        }

        detalle.setJustificacion(justificacion);
        detalle.setEstadoLinea(EstadoLinea.JUSTIFICADA); 
        
        detalle = detalleRepository.save(detalle);
        
        return conciliacionMapper.toDetalleResponseDto(detalle);
    }

    @Transactional
    public ConciliacionResponseDTO aplicarAjusteYCerrar(Long conciliacionId) {
        Conciliacion conciliacion = conciliacionRepository.findById(conciliacionId)
                .orElseThrow(() -> new NoSuchElementException("Conciliación no encontrada con ID: " + conciliacionId));
        
        if (conciliacion.getEstado() != EstadoConciliacion.ABIERTA) { 
            throw new ValidationException("La conciliación con ID " + conciliacionId + " no está abierta para el cierre. Estado actual: " + conciliacion.getEstado());
        }

        boolean quedanPendientes = conciliacion.getDetalles().stream()
                .anyMatch(d -> d.getEstadoLinea() == EstadoLinea.PENDIENTE); 
        
        if (quedanPendientes) {
            throw new ValidationException("No se puede cerrar la conciliación. Aún existen líneas sin justificar (Estado PENDIENTE).");
        }
        
        for (ConciliacionDetalle detalle : conciliacion.getDetalles()) {
            
            if (detalle.getDiferenciaCantidad() != null && 
                detalle.getDiferenciaCantidad().compareTo(BigDecimal.ZERO) != 0) {
                
                Bien bien = detalle.getBien();
                
                bien.setStockActual(detalle.getConteoFisico()); 
                bienRepository.save(bien);

                detalle.setEstadoLinea(EstadoLinea.AJUSTADA);
                detalleRepository.save(detalle); 
            }
        }
        
        conciliacion.setEstado(EstadoConciliacion.CERRADA);
        Conciliacion cerrada = conciliacionRepository.save(conciliacion);
        
        return conciliacionMapper.toResponseDto(cerrada);
    }
}