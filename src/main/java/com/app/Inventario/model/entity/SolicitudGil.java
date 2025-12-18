package com.app.Inventario.model.entity;

import com.app.Inventario.model.entityMaestras.Cuentadante;
import com.app.Inventario.model.enums.DestinoBienes;
import com.app.Inventario.model.enums.EstadoSolicitud;
import com.app.Inventario.model.enums.TipoCuentadante;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "solicitudes_gil")
public class SolicitudGil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_solicitud", nullable = false,unique = true,length = 50)
    private String codigo;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDate fecha;

    @Builder.Default
    @Column(name = "codigo_regional",nullable = false, length = 5)
    private String codigoRegional = "63";

    @Builder.Default
    @Column(name = "nombre_regional", length = 50, nullable = false)
    private String nombreRegional = "Quindío";

    @Builder.Default
    @Column(name = "codigo_centro_costos", length = 20, nullable = false)
    private String codigoCentroCostos = "953810";

    @Builder.Default
    @Column(name = "nombre_centro_costos", length = 100, nullable = false)
    private String nombreCentroCostos = "Comercio y Turismo";

    @Column(name = "jefe_oficina", length = 150)
    private String jefeOficina;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('UNIPERSONAL','MULTIPLE') DEFAULT 'UNIPERSONAL'")
    private TipoCuentadante tipoCuentadante = TipoCuentadante.UNIPERSONAL;


    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(columnDefinition = " ENUM('FORMACION', 'ADMINISTRATIVO') DEFAULT 'FORMACION'")
    private DestinoBienes destinoBienes = DestinoBienes.FORMACION;


    @Column(name = "ficha_caracterizacion" , length = 50 )
    private String fichaCaracterizacion;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pre_factura_id", unique = true)
    private PreFactura preFactura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factura_global_id")
    private FacturaGlobal facturaGlobal;

    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estado = EstadoSolicitud.PENDIENTE;

    @OneToMany(mappedBy = "solicitudGil", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Cuentadante> cuentadantes= new LinkedHashSet<>();;

    @OneToMany(mappedBy = "solicitudGil", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<SolicitudItems> items = new LinkedHashSet<>();
}

