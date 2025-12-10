package com.app.Inventario.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PreFacturaRequestDTO {
    @NotBlank(message = "El número de factura es obligatorio")
    private String numero;

    @NotNull(message = "El ID del programa es obligatorio")
    private Long programaId;

    @NotBlank(message = "El nombre del instructor es obligatorio")
    private String instructorNombre;

    @NotBlank(message = "La identificación del instructor es obligatoria")
    private String instructorIdentificacion;

    @Min(value = 0, message = "El total no puede ser negativo")
    private BigDecimal totalGeneral;

}
