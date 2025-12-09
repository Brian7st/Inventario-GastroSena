package com.app.Inventario.model.dto;

import com.app.Inventario.model.entity.EstadoBien;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "BienRequestDTO",
        description = "DTO utilizado para crear o actualizar bienes en el inventario."
)
public class BienRequestDTO {

    @Schema(
            description = "Código único del bien dentro del inventario",
            example = "UT-CH-001"
    )
    @NotBlank(message = "El código es obligatorio")
    private String codigo;

    @Schema(
            description = "Nombre del bien",
            example = "Cuchillo Chef 8 pulgadas"
    )
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Schema(
            description = "ID de la categoría a la que pertenece el bien",
            example = "5"
    )
    @NotNull(message = "Debes especificar el ID de la categoría")
    @Positive(message = "El ID de la categoría debe ser un número positivo")
    private Long categoriaId;

    @Schema(
            description = "Unidad de medida utilizada para el bien",
            example = "Unidad"
    )
    @NotBlank(message = "La unidad de medida es obligatoria")
    private String unidadMedida;

    @Schema(
            description = "Valor unitario del bien",
            example = "35000.00"
    )
    @NotNull(message = "El valor unitario es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El valor no puede ser negativo")
    private BigDecimal valorUnitario;

    @Schema(
            description = "Porcentaje de IVA aplicado al bien",
            example = "19.0"
    )
    @DecimalMin(value = "0.0", inclusive = true, message = "El IVA no puede ser negativo")
    private BigDecimal porcentajeIva;

    @Schema(
            description = "Cantidad actual disponible en inventario",
            example = "50"
    )
    @DecimalMin(value = "0.0", inclusive = true, message = "El stock no puede ser negativo")
    private BigDecimal stockActual;

    @Schema(
            description = "Cantidad mínima recomendada antes de generar alerta",
            example = "10"
    )
    @DecimalMin(value = "0.0", inclusive = true, message = "El stock mínimo no puede ser negativo")
    private BigDecimal stockMinimo;

    @Schema(
            description = "Estado actual del bien",
            example = "ACTIVO"
    )
    private EstadoBien estado;

}
