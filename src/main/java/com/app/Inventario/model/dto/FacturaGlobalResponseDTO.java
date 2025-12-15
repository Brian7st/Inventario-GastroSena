package com.app.Inventario.model.dto;

import com.app.Inventario.model.enums.EstadoFacturaGlobal;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor; 

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor 
@AllArgsConstructor 
@Schema(
        name = "FacturaGlobalResponseDTO",
        description = "DTO que representa la Factura Global consolidada."
)
public class FacturaGlobalResponseDTO {

    private Long id;
    private String numeroFactura;
    private LocalDateTime fechaGeneracion;
    
    private EstadoFacturaGlobal estado; 
    
    private BigDecimal subtotal;
    private BigDecimal valorIva;
    private BigDecimal totalGeneral;

    private List<Long> gilF014Asociados;

    private List<ConsolidacionItemResponseDTO> itemsConsolidados;
}