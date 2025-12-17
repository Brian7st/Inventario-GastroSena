package com.app.Inventario.mapper;

import com.app.Inventario.model.dto.CategoriaDTO;
import com.app.Inventario.model.entityMaestras.Categoria;
import org.springframework.stereotype.Component;

@Component
public class CatalogoMapper {

    public CategoriaDTO toCategoriaDTO(Categoria categoria){
        if(categoria == null)return null;

        return CategoriaDTO.builder()
                .id(categoria.getId())
                .nombre(categoria.getNombre())
                .descripcion(categoria.getDescripcion())
                .activo(categoria.getActivo())
                .build();
    }

    public Categoria toCategoriaEntity(CategoriaDTO dto){
        if(dto == null) return null;

        return Categoria.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .activo(dto.getActivo() != null ? dto.getActivo() : true)
                .build();
    }

    public void updateCategoriaFromDto(Categoria categoria, CategoriaDTO dto){
        if(dto == null ) return;

        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());

        if(dto.getActivo() != null){
            categoria.setActivo(dto.getActivo());
        }
    }

}
