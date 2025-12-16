package com.app.Inventario.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(
    name = "ConsolidacionItemDTO",
    description = "Representa un ítem de bien consolidado (agrupado por código) para la Factura Global."
)
public class ConsolidacionItemResponseDTO {

    @Schema(description = "Código único del Bien/Producto.")
    private String codigoProducto;

    @Schema(description = "Nombre o descripción del Bien.")
    private String descripcion;

    @Schema(description = "Cantidad total consolidada de este bien en la Factura Global.")
    private BigDecimal cantidad;

    @Schema(description = "Valor unitario del bien ANTES de aplicar el IVA.")
    private BigDecimal valorUnitarioAntesIva;

    @Schema(description = "Porcentaje de IVA aplicado al bien.")
    private BigDecimal ivaPorcentaje;

    @Schema(description = "Total de la línea SIN IVA.")
    private BigDecimal totalSinIva;

    @Schema(description = "Monto total del IVA para esta línea.")
    private BigDecimal valorIva;

    @Schema(description = "Total de la línea CON IVA.")
    private BigDecimal totalConIva;
}