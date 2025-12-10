package com.app.Inventario.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.lang.reflect.Type;
import java.math.BigDecimal;

@Entity
@Data
@Table (name = "solicitud_gil_items")
public class SolicitudItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "cantidad" , precision = 15, scale = 2, nullable = false)
    private BigDecimal cantidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_gil_id", nullable = false)
    private SolicitudGil solicitudGil;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bien_id",nullable = false)
    private Bien bien;


    }


