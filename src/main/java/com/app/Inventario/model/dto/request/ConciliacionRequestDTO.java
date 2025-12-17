package com.app.Inventario.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ConciliacionRequestDTO")
public class ConciliacionRequestDTO {

    @Schema(description = "Fecha de corte para la conciliación", example = "2025-11-30")
    @NotNull(message = "La fecha de corte es obligatoria.")
    private LocalDate fechaCorte;

    @Schema(description = "Nombre o ID del responsable que realiza el conteo")
    @NotBlank(message = "El nombre del responsable es obligatorio.")
    private String responsable;

    @Schema(description = "Lista de bienes y su conteo físico.")
    @NotNull(message = "Se requiere el conteo de al menos un bien.")
    private List<ConteoFisicoRequestDTO> conteoFisico;
}