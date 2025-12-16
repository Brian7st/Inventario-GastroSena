package com.app.Inventario.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImpuestoDTO {
    private Integer id;
    private String nombre;
    private BigDecimal porcentaje; // "19.00"
    private String codigoFiscal;
    private Boolean activo;
}