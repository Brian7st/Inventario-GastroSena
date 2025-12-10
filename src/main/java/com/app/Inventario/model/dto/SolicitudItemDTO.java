package com.app.Inventario.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(name = "SolicitudItemDTO",
description = "DTO utilizado para generar solicitudes de bienes")
public class SolicitudItemDTO {

    @NotNull(message = "el ID del bien no puede ser nulo")
    private long BienId;

    @NotNull(message = "la cantidad es reqeurida para solicitar bienes ")
    @DecimalMin(value = "0.01", message = "la cantidad tiene que ser mayor de cero")
    private BigDecimal cantidad;

    private String nombreBien;


}
