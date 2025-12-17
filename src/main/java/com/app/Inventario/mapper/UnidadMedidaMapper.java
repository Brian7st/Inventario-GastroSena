package com.app.Inventario.mapper;

import com.app.Inventario.model.dto.UnidadMedidaDTO;
import com.app.Inventario.model.entityMaestras.UnidadMedida;
import org.springframework.stereotype.Component;

@Component
public class UnidadMedidaMapper {

    public UnidadMedidaDTO toUnidadMedidaDTO(UnidadMedida unidadMedida){
        if(unidadMedida == null) return null;

        return UnidadMedidaDTO.builder()
                .id(unidadMedida.getIdUnidad())
                .nombre(unidadMedida.getNombre())
                .abreviatura(unidadMedida.getAbreviatura())
                .codigoFiscal(unidadMedida.getCodigoFiscal())
                .activo(unidadMedida.getActivo())
                .build();
    }

    public UnidadMedida toUnidadMedidaEntity(UnidadMedidaDTO dto){
        if(dto == null) return null;

        return UnidadMedida.builder()
                .nombre(dto.getNombre())
                .abreviatura(dto.getAbreviatura())
                .codigoFiscal(dto.getCodigoFiscal())
                .activo(dto.getActivo() != null ? dto.getActivo() : true)
                .build();
    }

    public void updateUnidadMedidaFromDTO(UnidadMedida unidadMedida, UnidadMedidaDTO dto){
        if(dto == null)return;

        unidadMedida.setNombre(dto.getNombre());
        unidadMedida.setAbreviatura(dto.getAbreviatura());
        unidadMedida.setCodigoFiscal(dto.getCodigoFiscal());
        if(dto.getActivo() != null){
            unidadMedida.setActivo(unidadMedida.getActivo());
        }
    }


}
