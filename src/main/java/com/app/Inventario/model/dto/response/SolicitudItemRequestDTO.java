package com.app.Inventario.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Schema(name = "SolicitudItemRequestDTO",
description = "DTO utilizado para generar solicitudes de bienes")

@AllArgsConstructor
@NoArgsConstructor
public class SolicitudItemRequestDTO {

    @NotNull(message = "el ID del bien no puede ser nulo")
    private Long bienId;

    @NotNull(message = "la cantidad es requerida para solicitar bienes ")
    @DecimalMin(value = "0.01", message = "la cantidad tiene que ser mayor de cero")
    private BigDecimal cantidad;

    private String nombreBien;


}
