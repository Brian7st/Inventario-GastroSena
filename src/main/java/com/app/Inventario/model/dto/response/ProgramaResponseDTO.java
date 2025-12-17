package com.app.Inventario.model.dto.response;

import com.app.Inventario.model.enums.EstadoPrograma;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgramaResponseDTO {
    private Long id;
    private String nombre;
    private String codigoFicha;
    private EstadoPrograma estadoPrograma;


}
