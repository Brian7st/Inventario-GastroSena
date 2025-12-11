package com.app.Inventario.model.dto;


import com.app.Inventario.model.entity.ProgramaFormacion;
import com.app.Inventario.model.enums.EstadoPreFactura;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data

public class PreFacturaResponseDTO {
    private Long id;
    private String numero;
    private LocalDateTime fecha;

    private String instructorNombre;
    private String instructorIdentificacion;

    private EstadoPreFactura estado;
    private BigDecimal totalGeneral;


    private ProgramaFormacion programa;
}
