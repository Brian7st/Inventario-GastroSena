package com.app.Inventario.mapper;

import com.app.Inventario.model.dto.PreFacturaRequestDTO;
import com.app.Inventario.model.dto.PreFacturaResponseDTO;
import com.app.Inventario.model.entity.PreFactura;

public class PreFacturaMapper {


    public PreFacturaResponseDTO toResponseDtoPreFactura(PreFactura preFactura){
        if(preFactura == null){
            return null;
        }

        PreFacturaResponseDTO dto = new PreFacturaResponseDTO();

        dto.setId(preFactura.getId());
        dto.setNumero(preFactura.getNumero());
        dto.setFecha(preFactura.getFecha());
        dto.setInstructorNombre(preFactura.getNombreInstructor());
        dto.setInstructorIdentificacion(preFactura.getIdentificacionInstructor());
        dto.setEstado(preFactura.getEstado());
        dto.setTotalGeneral(preFactura.getTotalPrefactura());
        dto.setPrograma(preFactura.getProgamaFormacion());



    }



}
