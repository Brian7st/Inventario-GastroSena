package com.app.Inventario.model.dto.response;

import com.app.Inventario.model.enums.TipoMovimiento;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos históricos de un movimiento de inventario")
public class MovimientoResponseDTO {

    @Schema(description = "ID único del movimiento", example = "1")
    private Long id;

    @Schema(description = "Fecha y hora del registro", example = "2024-12-15T10:30:00")
    private LocalDateTime fecha;

    @Schema(description = "Tipo de operación", example = "ENTRADA")
    private TipoMovimiento tipo;

    @Schema(description = "ID del bien", example = "15")
    private Long bienId;
    @Schema(description = "Código del bien", example = "TEC-001")
    private String bienCodigo;
    @Schema(description = "Nombre del bien", example = "Monitor 24 Pulgadas")
    private String bienNombre;

    @Schema(description = "Cantidad movida", example = "5.00")
    private BigDecimal cantidad;

    @Schema(description = "Valor unitario al momento del movimiento", example = "500000.00")
    private BigDecimal valorUnitario;

    @Schema(description = "Valor total de la operación (Cantidad * Unitario)", example = "2500000.00")
    private BigDecimal valorTotal;

    @Schema(description = "Stock antes de la operación", example = "10.00")
    private BigDecimal saldoAnterior;
    @Schema(description = "Stock resultante", example = "15.00")
    private BigDecimal saldoNuevo;

    @Schema(description = "Documento de referencia", example = "REMISION-001")
    private String fichaEntrega;
    @Schema(description = "Consecutivo o Factura", example = "FAC-123")
    private String consecutivo;
    @Schema(description = "Observaciones", example = "Ingreso por compra mensual")
    private String observaciones;
}