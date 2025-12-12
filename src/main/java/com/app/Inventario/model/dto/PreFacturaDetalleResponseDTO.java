package com.app.Inventario.model.dto;

import com.app.Inventario.model.entity.Bien;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema (name = "PreFacturaDetalleResponseDTO",
        description = "DTO de respuesta que representa una línea de detalle, incluyendo la entidad Bien asociada.")

public class PreFacturaDetalleResponseDTO {

    private Long id;
    private Bien bien;

    @Schema( description = "Cantidad del bien solicitada")
    private BigDecimal cantidad;

    @Schema(description = "Precio unitario adjudicado al bien.")
    private BigDecimal precioAdjudicado;

    @Schema(description = "Valor total de la línea (calculado).")
    private BigDecimal totalLinea;
}