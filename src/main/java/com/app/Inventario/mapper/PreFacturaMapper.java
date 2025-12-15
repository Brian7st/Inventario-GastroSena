package com.app.Inventario.mapper;

import com.app.Inventario.model.dto.PreFacturaRequestDTO;
import com.app.Inventario.model.dto.PreFacturaResponseDTO;
import com.app.Inventario.model.entity.PreFactura;
import com.app.Inventario.model.entity.ProgramaFormacion;

public class PreFacturaMapper {


    public PreFacturaResponseDTO toResponseDtoPreFactura(PreFactura preFactura) {
        if (preFactura == null) {
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
        dto.setPrograma(preFactura.getProgramaFormacion());


        return dto;
    }


    public PreFactura toEntity(PreFacturaRequestDTO dto, ProgramaFormacion programaFormacion) {
        if(dto == null){
            return null;
        }

        PreFactura preFactura = new PreFactura();

        preFactura.setNumero(dto.getNumero());
        preFactura.setNombreInstructor(dto.getInstructorNombre());
        preFactura.setIdentificacionInstructor(dto.getInstructorIdentificacion());
        preFactura.setTotalPrefactura(dto.getTotalGeneral());
        preFactura.setProgramaFormacion(programaFormacion);


        return preFactura;
    }

    public void updateEntityFromDTO(PreFactura PreFacturaExistente, PreFacturaRequestDTO dto, ProgramaFormacion programaFormacion) {
        PreFacturaExistente.setNumero(dto.getNumero());
        PreFacturaExistente.setNombreInstructor(dto.getInstructorNombre());
        PreFacturaExistente.setIdentificacionInstructor(dto.getInstructorIdentificacion());
        PreFacturaExistente.setTotalPrefactura(dto.getTotalGeneral());
        PreFacturaExistente.setProgramaFormacion(programaFormacion);

        PreFacturaExistente.setProgramaFormacion(programaFormacion);



    }


}
