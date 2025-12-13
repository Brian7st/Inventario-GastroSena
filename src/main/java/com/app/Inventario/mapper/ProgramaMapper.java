package com.app.Inventario.mapper;

import com.app.Inventario.model.dto.ProgramaRequestDTO;
import com.app.Inventario.model.dto.ProgramaResponseDTO;
import com.app.Inventario.model.entity.ProgramaFormacion;
import com.app.Inventario.model.enums.EstadoPrograma;
import org.springframework.stereotype.Component;

@Component
public class ProgramaMapper {

    public ProgramaResponseDTO toResponseDto(ProgramaFormacion programa) {
        if (programa == null) {
            return null;
        }

        ProgramaResponseDTO dto = new ProgramaResponseDTO();

        dto.setId(programa.getId());
        dto.setNombre(programa.getNombre());
        dto.setCodigoFicha(programa.getCodigoFicha());
        dto.setEstadoPrograma(programa.getEstadoPrograma());

        return dto;
    }

    public ProgramaFormacion toEntity(ProgramaRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        ProgramaFormacion programa = new ProgramaFormacion();

        programa.setNombre(dto.getNombre());
        programa.setCodigoFicha(dto.getCodigoFicha());

        if (dto.getEstadoPrograma() != null) {
            programa.setEstadoPrograma(dto.getEstadoPrograma());
        } else {
            programa.setEstadoPrograma(EstadoPrograma.ACTIVO);
        }

        return programa;
    }

    public void updateEntityFromDTO(ProgramaFormacion programaExistente, ProgramaRequestDTO dto) {

        programaExistente.setNombre(dto.getNombre());
        programaExistente.setCodigoFicha(dto.getCodigoFicha());

        if (dto.getEstadoPrograma() != null) {
            programaExistente.setEstadoPrograma(dto.getEstadoPrograma());
        }
    }
}