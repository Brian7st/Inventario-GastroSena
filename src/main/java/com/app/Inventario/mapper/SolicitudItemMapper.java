package com.app.Inventario.mapper;

import com.app.Inventario.model.dto.response.SolicitudItemResponseDTO;
import com.app.Inventario.model.entity.SolicitudItems;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SolicitudItemMapper {
    private final BienMapper bienMapper;

    public SolicitudItemMapper(BienMapper bienMapper){
        this.bienMapper = bienMapper;
    }


    public SolicitudItemResponseDTO toResponseDto(SolicitudItems solicitudItems){

        if (solicitudItems == null){
            return null;
        }
        SolicitudItemResponseDTO dto = new SolicitudItemResponseDTO();

        dto.setCantidad(solicitudItems.getCantidad());
        dto.setBien(bienMapper.toResponseDto(solicitudItems.getBien()));
        return dto;
    }

    public List<SolicitudItemResponseDTO> toResponseDtos(List<SolicitudItems> items){
        if (items== null){
            return null;
        }else {
            return items.stream()
                    .map(this::toResponseDto)
                    .collect(Collectors.toList());
        }
    }

}
