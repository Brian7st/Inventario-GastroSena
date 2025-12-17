package com.app.Inventario.mapper;

import com.app.Inventario.model.dto.response.MovimientoResponseDTO;
import com.app.Inventario.model.entity.Movimiento;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovimientoMapper {

    public MovimientoResponseDTO toResponseDto(Movimiento movimiento) {
        if (movimiento == null) return null;

        return MovimientoResponseDTO.builder()
                .id(movimiento.getId())
                .fecha(movimiento.getFechaMovimiento())
                .tipo(movimiento.getTipo())


                .bienId(movimiento.getBien() != null ? movimiento.getBien().getId() : null)
                .bienCodigo(movimiento.getBien() != null ? movimiento.getBien().getCodigo() : null)
                .bienNombre(movimiento.getBien() != null ? movimiento.getBien().getNombre() : null)

                .cantidad(movimiento.getCantidad())
                .valorUnitario(movimiento.getValorUnitario())
                .valorTotal(movimiento.getValorTotal())

                .saldoAnterior(movimiento.getSaldoAnterior())
                .saldoNuevo(movimiento.getSaldoNuevo())

                .fichaEntrega(movimiento.getFichaEntrega())
                .consecutivo(movimiento.getConsecutivo())
                .observaciones(movimiento.getObservaciones())
                .build();
    }

    public List<MovimientoResponseDTO> toResponseList(List<Movimiento> movimientos) {
        if (movimientos == null) return List.of();
        return movimientos.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
}