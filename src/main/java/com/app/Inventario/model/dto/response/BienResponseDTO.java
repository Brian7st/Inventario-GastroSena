package com.app.Inventario.model.dto.response;

import com.app.Inventario.model.enums.EstadoBien;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BienResponseDTO {

    private Long idBien;
    private String codigo;
    private String codAlmacen;
    private String nombre;
    private String descripcion;

    private Integer categoriaId;
    private String categoriaNombre;
    private Integer unidadId;
    private String unidadNombre;
    private Integer impuestoId;
    private String impuestoNombre;
    private BigDecimal impuestoPorcentaje;

    private BigDecimal valorUnitario;
    private BigDecimal precioVentaFinal;
    private BigDecimal stockActual;
    private BigDecimal valorTotalStock;
    private BigDecimal stockMinimo;
    private EstadoBien estado;
    private Boolean activo;
}