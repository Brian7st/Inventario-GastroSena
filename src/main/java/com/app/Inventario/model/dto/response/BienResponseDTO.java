package com.app.Inventario.model.dto.response;

import com.app.Inventario.model.enums.EstadoBien;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(
        name = "BienResponseDTO",
        description = "DTO de salida con cálculos contables e información detallada."
)
public class BienResponseDTO {

    @Schema(description = "ID interno del sistema", example = "12")
    private Long idBien;

    @Schema(description = "Código SKU", example = "UT-CH-001")
    private String codigo;

    @Schema(description = "Ubicación en almacén", example = "EST-B-2")
    private String codAlmacen;

    @Schema(description = "Nombre del bien", example = "Cuchillo Chef 8 pulgadas")
    private String nombre;

    private String descripcion;

    @Schema(description = "ID de la categoría", example = "5")
    private Integer categoriaId;

    @Schema(description = "Nombre de la categoría", example = "Utensilios")
    private String categoriaNombre;

    @Schema(description = "ID de la unidad de medida", example = "1")
    private Integer unidadId;

    @Schema(description = "Nombre/Abreviatura de la unidad", example = "Unidad (UND)")
    private String unidadNombre;



    @Schema(description = "ID del impuesto asociado", example = "2")
    private Integer impuestoId;

    @Schema(description = "Nombre del impuesto", example = "IVA General")
    private String impuestoNombre;

    @Schema(description = "Porcentaje del impuesto aplicado", example = "19.00")
    private BigDecimal impuestoPorcentaje;



    @Schema(description = "Precio Base (Sin Impuesto)", example = "35000.00")
    private BigDecimal valorUnitario;

    // Este campo es calculado en Java para ayudar al Front (Precio Base + % Impuesto)
    @Schema(description = "Precio de Venta con Impuesto incluido (Calculado)", example = "41650.00")
    private BigDecimal precioVentaFinal;

    // --- Inventario (Datos Críticos) ---

    @Schema(description = "Stock físico actual (Calculado por DB/Trigger)", example = "50.00")
    private BigDecimal stockActual;

    @Schema(description = "Valor total del inventario (Stock * Costo). Campo STORED de DB", example = "1750000.00")
    private BigDecimal valorTotalStock;

    @Schema(description = "Punto de reorden", example = "10.00")
    private BigDecimal stockMinimo;

    @Schema(description = "Estado del inventario", example = "DISPONIBLE")
    private EstadoBien estado;

    private Boolean activo;
}