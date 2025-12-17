package com.app.Inventario.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "FacturaGlobalRequestDTO",
        description = "DTO utilizado por la Contadora para seleccionar y solicitar la consolidación de solicitudes GIL-F-014."
)
public class FacturaGlobalRequestDTO {

    @Schema(
            description = "Lista de IDs de las solicitudes GIL-F-014 que se desean consolidar en una Factura Global.",
            example = "[15, 18, 22]"
    )
    @NotBlank(message = "La lista de IDs es obligatoria.")
    @NotEmpty(message = "Debe especificar al menos un ID de solicitud GIL-F-014.")
    private List<Long> gilF014Ids;

}