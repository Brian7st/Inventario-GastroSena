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
@Schema(name ="PreFacturaDetalleRequestDTO",
        description = "DTO que indica un producto y la cantidad correspondiente dentro de una línea de la prefactura"
)

public class PreFacturaDetalleRequestDTO {

    @Schema( description = "ID del bien de inventario que incluye en la prefactura",
    example = "12"
    )
    @NotNull(message = "El ID del bien es obligatorio")
    @Positive(message = "El ID del bien debe ser un número positivo")
    private Long bienId;

    @Schema(
            description = "Cantidad del bien solicitada en esta línea.",
            example = "5.00"
    )
    @NotNull(message = "La cantidad es obligatoria")
    @DecimalMin(value = "0.01", inclusive = true, message = "La cantidad debe ser mayor a cero")
    private BigDecimal cantidad;

    @Schema(
            description = "Precio unitario adjudicado al bien en esta prefactura (precio final de venta o traslado).",
            example = "34500.00"
    )
    @NotNull(message = "El precio adjudicado es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El precio adjudicado no puede ser negativo")
    private BigDecimal precioAdjudicado;

}