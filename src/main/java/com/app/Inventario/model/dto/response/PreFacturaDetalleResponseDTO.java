package com.app.Inventario.model.dto.response;

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
        description = "DTO de respuesta que representa una línea de detalle, incluyendo los datos esenciales del Bien.")

public class PreFacturaDetalleResponseDTO {

    private Long id;

    @Schema(description = "ID del Bien asociado a este detalle.")
    private Long bienId;

    @Schema(description = "Código del bien.")
    private String codigoBien;

    @Schema(description = "Nombre del bien.")
    private String nombreBien;

    @Schema( description = "Cantidad del bien solicitada")
    private BigDecimal cantidad;

    @Schema(description = "Precio unitario adjudicado al bien.")
    private BigDecimal precioAdjudicado;

    @Schema(description = "Valor total de la línea (calculado).")
    private BigDecimal totalLinea;
}