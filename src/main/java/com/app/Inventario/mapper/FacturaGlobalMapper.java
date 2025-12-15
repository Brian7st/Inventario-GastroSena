package com.app.Inventario.mapper;

import com.app.Inventario.model.dto.ConsolidacionItemResponseDTO;
import com.app.Inventario.model.dto.FacturaGlobalResponseDTO;
import com.app.Inventario.model.entity.FacturaGlobal;
import com.app.Inventario.model.entity.SolicitudGil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FacturaGlobalMapper {

    public FacturaGlobalResponseDTO toResponseDto(
            FacturaGlobal factura,
            List<ConsolidacionItemResponseDTO> items,
            List<SolicitudGil> solicitudes) {

        if (factura == null) {
            return null;
        }

        List<Long> gilIds = solicitudes.stream()
                .map(SolicitudGil::getId)
                .collect(Collectors.toList());

        return FacturaGlobalResponseDTO.builder()
                .id(factura.getId())
                .numeroFactura(factura.getNumeroConsecutivo())
                .fechaGeneracion(factura.getFechaGeneracion())
                .totalGeneral(factura.getTotalFinal())
                .estado(factura.getEstado())
                .itemsConsolidados(items)
                .gilF014Asociados(gilIds)
                .build();
    }

    public FacturaGlobalResponseDTO toResponseDtoSimple(FacturaGlobal factura, List<SolicitudGil> solicitudes) {
        if (factura == null) {
            return null;
        }

        List<Long> gilIds = solicitudes.stream()
                .map(SolicitudGil::getId)
                .collect(Collectors.toList());

        return FacturaGlobalResponseDTO.builder()
                .id(factura.getId())
                .numeroFactura(factura.getNumeroConsecutivo())
                .fechaGeneracion(factura.getFechaGeneracion())
                .totalGeneral(factura.getTotalFinal())
                .estado(factura.getEstado())
                .itemsConsolidados(List.of())
                .gilF014Asociados(gilIds)
                .build();
    }
}