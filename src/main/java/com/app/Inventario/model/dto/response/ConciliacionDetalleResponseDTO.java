package com.app.Inventario.model.dto.response;

import com.app.Inventario.model.enums.EstadoLinea;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "ConciliacionDetalleResponseDTO")
public class ConciliacionDetalleResponseDTO {

    private Long id;
    private Long bienId;
    private String bienNombre;

    @Schema(description = "Cantidad registrada en el sistema a la fecha de corte.")
    private BigDecimal conteoSistema;

    @Schema(description = "Cantidad reportada por el conteo físico.")
    private BigDecimal conteoFisico;

    @Schema(description = "Diferencia de cantidad (Conteo Físico - Conteo Sistema).")
    private BigDecimal diferenciaCantidad;

    @Schema(description = "Valor unitario del bien (para cálculo de la diferencia).")
    private BigDecimal valorUnitario;

    @Schema(description = "Valor total de la diferencia (Diferencia Cantidad * Valor Unitario).")
    private BigDecimal valorDiferencia;

    @Schema(description = "Estado de la línea (PENDIENTE, JUSTIFICADA, AJUSTADA)")
    private EstadoLinea estadoLinea;

    @Schema(description = "Justificación si el estado es JUSTIFICADA.")
    private String justificacion;
}