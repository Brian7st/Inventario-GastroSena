package com.app.Inventario.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoriaDTO {
    private Integer id;
    private String nombre;
    private String descripcion;
    private Boolean activo;
}
