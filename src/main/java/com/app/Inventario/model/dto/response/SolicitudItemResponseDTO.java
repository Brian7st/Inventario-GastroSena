package com.app.Inventario.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudItemResponseDTO {
    private Long id;
    private BigDecimal cantidad;
    private BigDecimal precioCongelado;
    private BigDecimal totalLinea;

    private Long bienId;
    private String bienNombre;
    private String bienCodigo;
}