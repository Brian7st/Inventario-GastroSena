package com.app.Inventario.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnidadMedidaDTO {
    private Integer id;
    private String nombre;
    private String abreviatura; // "KG", "UND"
    private String codigoFiscal; // Para facturación elect.
    private Boolean activo;
}
