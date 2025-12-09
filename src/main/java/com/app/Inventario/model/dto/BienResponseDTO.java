package com.app.Inventario.model.dto;

import com.app.Inventario.model.entity.EstadoBien;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        name = "BienResponseDTO",
        description = "DTO que representa la información completa de un bien almacenado en el inventario."
)
public class BienResponseDTO {

    @Schema(
            description = "Identificador único del bien",
            example = "12"
    )
    private Long id;

    @Schema(
            description = "Código único del bien",
            example = "UT-CH-001"
    )
    private String codigo;

    @Schema(
            description = "Nombre del bien",
            example = "Cuchillo Chef 8 pulgadas"
    )
    private String nombre;

    @Schema(
            description = "ID de la categoría a la que pertenece el bien",
            example = "5"
    )
    private Long categoriaId;

    @Schema(
            description = "Nombre de la categoría asociada al bien",
            example = "Utensilios de Cocina"
    )
    private String categoriaNombre;

    @Schema(
            description = "Unidad de medida utilizada",
            example = "Unidad"
    )
    private String unidadMedida;

    @Schema(
            description = "Valor unitario del bien",
            example = "35000.00"
    )
    private BigDecimal valorUnitario;

    @Schema(
            description = "Porcentaje de IVA aplicado al bien",
            example = "19.0"
    )
    private BigDecimal porcentajeIva;

    @Schema(
            description = "Valor total unitario del bien con IVA incluido",
            example = "41650.00"
    )
    private BigDecimal valorConIva;

    @Schema(
            description = "Cantidad actual disponible del bien en inventario",
            example = "50"
    )
    private BigDecimal stockActual;

    @Schema(
            description = "Stock mínimo permitido antes de generar alertas",
            example = "10"
    )
    private BigDecimal stockMinimo;

    @Schema(
            description = "Estado actual del bien",
            example = "ACTIVO"
    )
    private EstadoBien estado;
}
