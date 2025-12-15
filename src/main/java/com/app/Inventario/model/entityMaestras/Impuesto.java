package com.app.Inventario.model.entityMaestras;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "impuestos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Impuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nombre; // Ej: "IVA General", "Exento", "Impoconsumo"

    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal porcentaje;

    @Column(name = "codigo_fiscal", length = 20)
    private String codigoFiscal;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;

}