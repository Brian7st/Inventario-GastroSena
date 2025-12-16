package com.app.Inventario.model.dto;

import com.app.Inventario.model.enums.EstadoSolicitud;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EstadoSolicitudUpdateDTO {
    @NotNull(message = "El nuevo estado no puede ser nulo")
    private EstadoSolicitud nuevoEstado;
}
