package com.app.Inventario.model.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    // Validación anidada y de tamaño mínimo
    @NotNull(message = "Los detalles de la prefactura son obligatorios")
    @Size(min = 1, message = "Debe incluir al menos un detalle de bien en la prefactura.")
    @Valid
    private List<PreFacturaDetalleRequestDTO> detalles;
}

