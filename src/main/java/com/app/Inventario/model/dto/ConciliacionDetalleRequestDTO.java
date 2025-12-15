package com.app.Inventario.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "ConciliacionRequestDTO",
        description = "DTO utilizado para iniciar un nuevo proceso de conciliación de inventario."
)
public class ConciliacionDetalleRequestDTO {

    @Schema(
            description = "Fecha específica de corte para la cual se realiza la conciliación.",
            example = "2025-12-31"
    )
    @NotNull(message = "La fecha de corte es obligatoria.")
    private LocalDate fechaCorte;

    @Schema(
            description = "Nombre o identificador del usuario responsable de realizar y registrar el conteo físico.",
            example = "Auditoría de Inventario 001"
    )
    @NotBlank(message = "El nombre del responsable es obligatorio.")
    private String responsable;

    @Schema(
            description = "Lista de los bienes y su cantidad real contada en el inventario físico."
    )
    @NotNull(message = "La lista de conteo físico es obligatoria.")
    @NotEmpty(message = "Debe registrar al menos un bien en el conteo.")
    @Valid
    private List<ConteoFisicoRequestDTO> conteoFisico;
}