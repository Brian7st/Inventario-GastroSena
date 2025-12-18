package com.app.Inventario.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "solicitud_gil_items")
public class SolicitudItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_gil_id", nullable = false)
    @JsonIgnore
    private SolicitudGil solicitudGil;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bien_id",nullable = false)
    private Bien bien;


    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal cantidad;

    @Column(name = "precio_congelado", nullable = false, precision = 15, scale = 2)
    private BigDecimal precioCongelado;

    @Column(name = "total_linea", precision = 15, scale = 2)
    private BigDecimal totalLinea;


    }


