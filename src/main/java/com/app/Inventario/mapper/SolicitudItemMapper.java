package com.app.Inventario.mapper;

import com.app.Inventario.model.dto.response.SolicitudItemResponseDTO;
import com.app.Inventario.model.entity.SolicitudItems;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SolicitudItemMapper {

    public SolicitudItemResponseDTO toResponseDto(SolicitudItems item) {
        if (item == null) {
            return null;
        }

        SolicitudItemResponseDTO dto = new SolicitudItemResponseDTO();
        dto.setId(item.getId());
        dto.setCantidad(item.getCantidad());

        dto.setPrecioCongelado(item.getPrecioCongelado());
        dto.setTotalLinea(item.getTotalLinea());

        if (item.getBien() != null) {
            dto.setBienId(item.getBien().getId());
            dto.setBienNombre(item.getBien().getNombre());
            dto.setBienCodigo(item.getBien().getCodigo());
        }

        return dto;
    }

    public List<SolicitudItemResponseDTO> toResponseDtos(Collection<SolicitudItems> items) {
        if (items == null) return List.of();
        return items.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
}