package com.app.Inventario.mapper;

import com.app.Inventario.model.dto.response.SolicitudItemResponseDTO;
import com.app.Inventario.model.entity.SolicitudItems;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SolicitudItemMapper {


    public SolicitudItemResponseDTO toResponseDto(SolicitudItems solicitudItems){
        if (solicitudItems == null){
            return null;
        }

        SolicitudItemResponseDTO dto = new SolicitudItemResponseDTO();
        dto.setId(solicitudItems.getId());
        dto.setCantidad(solicitudItems.getCantidad());


        if (solicitudItems.getBien() != null) {
            dto.setBienId(solicitudItems.getBien().getId());
            dto.setBienNombre(solicitudItems.getBien().getNombre());
            dto.setBienCodigo(solicitudItems.getBien().getCodigo());
        }

        return dto;
    }

    public List<SolicitudItemResponseDTO> toResponseDtos(List<SolicitudItems> items){
        if (items == null){
            return null;
        }
        return items.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
}