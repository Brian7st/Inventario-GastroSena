package com.app.Inventario.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Solicitud para registrar movimientos masivos (Entradas o Salidas)")
public class RegistroMovimientoDTO {

    @Schema(description = "Referencia del documento de entrega (Obligatorio para salidas)", example = "ACTA-2024-005")
    @Size(max = 50, message = "La ficha de entrega no puede exceder 50 caracteres")
    private String fichaEntrega;

    @Schema(description = "Número de factura o consecutivo interno", example = "FAC-9988")
    @Size(max = 50, message = "El consecutivo no puede exceder 50 caracteres")
    private String consecutivo;

    @Schema(description = "Lista de bienes a procesar")
    @NotEmpty(message = "Debe enviar al menos un detalle de movimiento")
    @Valid
    private List<DetalleMovimientoDTO> detalles;
}