package com.app.Inventario.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "ConteoFisicoDTO",
        description = "Representa el reporte del conteo físico de un bien específico para la conciliación."
)
public class ConteoFisicoRequestDTO {

    @Schema(
            description = "ID del bien que se está contando, debe existir en la tabla 'bienes'.",
            example = "105"
    )
    @NotNull(message = "El ID del bien es obligatorio.")
    @Positive(message = "El ID del bien debe ser un número positivo.")
    private Long bienId;

    @Schema(
            description = "Cantidad física real encontrada en el inventario.",
            example = "45.0"
    )
    @NotNull(message = "La cantidad reportada es obligatoria.")
    @DecimalMin(value = "0.0", inclusive = true, message = "La cantidad física no puede ser negativa.")
    private BigDecimal cantidadReportada;
}