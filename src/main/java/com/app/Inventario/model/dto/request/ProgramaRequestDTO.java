package com.app.Inventario.model.dto.request;

import com.app.Inventario.model.enums.EstadoPrograma;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgramaRequestDTO {
    @NotBlank(message = "El nombre del programa es obligatorio")
    private String nombre;

    @NotBlank(message = "El código de ficha es obligatorio")
    private String codigoFicha;

    @NotNull(message = "El estado es requerido y debe ser ACTIVO o INACTIVO")
    private EstadoPrograma estadoPrograma;


}
