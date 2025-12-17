package com.app.Inventario.model.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudItemResponseDTO {

    private BigDecimal cantidad;

    private BienResponseDTO bien;


}
