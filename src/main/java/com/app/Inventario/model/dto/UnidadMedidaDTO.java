package com.app.Inventario.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UnidadMedidaDTO {
    private Integer id;
    private String nombre;
    private String abreviatura; // "KG", "UND"
    private String codigoFiscal; // Para facturación elect.
    private Boolean activo;
}
