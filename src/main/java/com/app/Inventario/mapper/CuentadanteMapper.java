package com.app.Inventario.mapper;

import com.app.Inventario.model.dto.CuentadanteDTO;
import com.app.Inventario.model.entityMaestras.Cuentadante;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CuentadanteMapper {

    public CuentadanteDTO toResponseDto (Cuentadante cuentadante){
        if (cuentadante == null){

            return null;
        }
        CuentadanteDTO dto= new CuentadanteDTO();

        dto.setId(cuentadante.getId());
        dto.setNombre(cuentadante.getNombre());
        dto.setIdentificacion(cuentadante.getIdentificacion());

        return dto;
    }

    public List<CuentadanteDTO> toResponseDtos(Collection<Cuentadante> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
    public Cuentadante toEntity(CuentadanteDTO dto) {
        if (dto == null) {
            return null;
        }
        Cuentadante cuentadante = new Cuentadante();
        cuentadante.setNombre(dto.getNombre());
        cuentadante.setIdentificacion(dto.getIdentificacion());

        return cuentadante;
    }
}
