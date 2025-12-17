package com.app.Inventario.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name= "pre_factura_detalles")
@Data
@NoArgsConstructor
@AllArgsConstructor


public class PreFacturaDetalle{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="pre_factura_id",nullable = false)
    @JsonIgnore
    private PreFactura preFactura;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bien_id", nullable = false)
    private Bien bien;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal cantidad;

    @Column(name = "precio_adjudicado", nullable = false, precision = 15, scale = 2)
    private BigDecimal precioAdjudicado;

    @Column(name = "total_linea", insertable = false, updatable = false, precision = 15, scale = 2)
    private BigDecimal totalLinea;

}
