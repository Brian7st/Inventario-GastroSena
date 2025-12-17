package com.app.Inventario.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CuentadanteDTO {

    private Long id;

    private String nombre;

    private String identificacion;

}
