package com.app.Inventario.model.dto.request;

import com.app.Inventario.model.enums.EstadoBien;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        name = "BienRequestDTO",
        description = "DTO para registrar o actualizar bienes. Se usan IDs para referenciar catálogos."
)
public class BienRequestDTO {

    @Schema(description = "Código SKU único o código de barras", example = "UT-CH-001")
    @NotBlank(message = "El código es obligatorio")
    @Size(max = 50, message = "El código no puede exceder 50 caracteres")
    private String codigo;

    @Schema(description = "Ubicación física o código secundario", example = "PASILLO-A-01")
    @Size(max = 50, message = "El código de almacén no puede exceder 50 caracteres")
    private String codAlmacen;

    @Schema(description = "Nombre comercial del bien", example = "Cuchillo Chef 8 pulgadas")
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
    private String nombre;

    @Schema(description = "Descripción detallada del producto", example = "Acero inoxidable, mango ergonómico...")
    private String descripcion;


    @Schema(description = "ID de la categoría", example = "5")
    @NotNull(message = "El ID de la categoría es obligatorio")
    @Positive
    private Integer categoriaId;

    @Schema(description = "ID de la unidad de medida (Tabla unidades_medida)", example = "1")
    @NotNull(message = "El ID de la unidad de medida es obligatorio")
    @Positive
    private Integer unidadId;

    @Schema(description = "ID de la regla de impuesto (Tabla impuestos)", example = "2")
    @NotNull(message = "El ID del impuesto es obligatorio")
    @Positive
    private Integer impuestoId;


    @Schema(description = "Precio Base SIN Impuestos", example = "35000.00")
    @NotNull(message = "El valor unitario es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal valorUnitario;

    @Schema(description = "Stock Mínimo para alertas", example = "10")
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal stockMinimo;

    @Schema(description = "Estado manual del bien (Opcional)", example = "DISPONIBLE")
    private EstadoBien estado;

    @Schema(description = "Define si el bien está habilitado para transacciones", example = "true")
    private Boolean activo;
}