package com.app.Inventario.model.dto;


import lombok.Data;
import java.math.BigDecimal;

@Data
public class SolicitudItemResponseDTO {

    private BigDecimal cantidad;

    private BienResponseDTO bien;


}
