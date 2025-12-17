package com.app.Inventario.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Detalle individual de un producto dentro de una transacción de inventario")
public class DetalleMovimientoDTO {

    @Schema(description = "ID del bien a afectar", example = "15")
    @NotNull(message = "El ID del bien es obligatorio")
    private Long bienId;

    @Schema(description = "Cantidad de unidades a mover", example = "10.50")
    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a 0")
    private BigDecimal cantidad;

    @Schema(description = "Observación o motivo específico para este ítem", example = "Caja dañada")
    @Size(max = 255, message = "La observación no puede exceder 255 caracteres")
    private String observaciones;
}