package com.app.Inventario.mapper;

import com.app.Inventario.model.dto.ImpuestoDTO;
import com.app.Inventario.model.entityMaestras.Impuesto;
import org.springframework.stereotype.Component;

@Component
public class ImpuestoMapper {

    public ImpuestoDTO toImpuestoDTO(Impuesto impuesto){
        if(impuesto == null) return null;

        return ImpuestoDTO.builder()
                .id(impuesto.getId())
                .nombre(impuesto.getNombre())
                .porcentaje(impuesto.getPorcentaje())
                .codigoFiscal(impuesto.getCodigoFiscal())
                .activo(impuesto.getActivo())
                .build();
    }

    public Impuesto toImpuestoEntity(ImpuestoDTO dto){
        if(dto == null) return null;

        return Impuesto.builder()
                .nombre(dto.getNombre())
                .porcentaje(dto.getPorcentaje())
                .codigoFiscal(dto.getCodigoFiscal())
                .activo(dto.getActivo() != null? dto.getActivo() : true)
                .build();
    }


    public  void  updateImpuestoFromDTO(Impuesto impuesto, ImpuestoDTO dto){

        if(dto == null)return;

        impuesto.setNombre(dto.getNombre());
        impuesto.setCodigoFiscal(dto.getCodigoFiscal());
        if(dto.getActivo() != null){
            impuesto.setActivo(dto.getActivo());
        }
    }



}
