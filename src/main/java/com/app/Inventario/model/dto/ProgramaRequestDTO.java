package com.app.Inventario.model.dto;

import com.app.Inventario.model.enums.EstadoPrograma;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ProgramaRequestDTO {
    @NotBlank(message = "El nombre del programa es obligatorio")
    private String nombre;

    @NotBlank(message = "El código de ficha es obligatorio")
    private String codigoFicha;

    @Pattern(regexp = "ACTIVO|INACTIVO", message = "El estado debe ser ACTIVO o INACTIVO")
    private EstadoPrograma estadoPrograma;
}
