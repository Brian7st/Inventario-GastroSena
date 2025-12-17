package com.app.Inventario.mapper;

import com.app.Inventario.model.dto.response.ConciliacionDetalleResponseDTO;
import com.app.Inventario.model.dto.response.ConciliacionResponseDTO;
import com.app.Inventario.model.entity.Conciliacion;
import com.app.Inventario.model.entity.ConciliacionDetalle;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConciliacionMapper {

    public ConciliacionResponseDTO toResponseDto(Conciliacion conciliacion) {
        if (conciliacion == null) return null;

        List<ConciliacionDetalleResponseDTO> detallesDTO = conciliacion.getDetalles() != null
                ? conciliacion.getDetalles().stream().map(this::toDetalleResponseDto).collect(Collectors.toList())
                : null;

        return ConciliacionResponseDTO.builder()
                .id(conciliacion.getId())
                .fechaCorte(conciliacion.getFechaCorte())
                .responsable(conciliacion.getResponsable())
                .estado(conciliacion.getEstado())
                .totalDiferenciaValor(conciliacion.getTotalDiferenciaValor())
                .detalles(detallesDTO)
                .build();
    }

    public ConciliacionDetalleResponseDTO toDetalleResponseDto(ConciliacionDetalle detalle) {
        if (detalle == null) return null;

        return ConciliacionDetalleResponseDTO.builder()
                .id(detalle.getId())
                .bienId(detalle.getBien().getId())
                .bienNombre(detalle.getBien().getNombre())
                .conteoSistema(detalle.getConteoSistema())
                .conteoFisico(detalle.getConteoFisico())
                .diferenciaCantidad(detalle.getDiferenciaCantidad())
                .valorUnitario(detalle.getBien().getValorUnitario())
                .valorDiferencia(detalle.getValorDiferencia())
                .estadoLinea(detalle.getEstadoLinea())
                .justificacion(detalle.getJustificacion())
                .build();
    }

}