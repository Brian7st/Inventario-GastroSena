package com.app.Inventario.model.dto;

import com.app.Inventario.model.enums.EstadoConciliacion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "ConciliacionResponseDTO")
public class ConciliacionResponseDTO {

    private Long id;
    private LocalDate fechaCorte;
    private String responsable;
    private EstadoConciliacion estado;

    @Schema(description = "Total de la diferencia en valor monetario entre el sistema y el conteo físico.")
    private BigDecimal totalDiferenciaValor;

    @Schema(description = "Detalles de la conciliación por bien.")
    private List<ConciliacionDetalleResponseDTO> detalles;
}