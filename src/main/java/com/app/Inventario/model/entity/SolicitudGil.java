package com.app.Inventario.model.entity;

import com.app.Inventario.model.enums.DestinoBienes;
import com.app.Inventario.model.enums.EstadoSolicitud;
import com.app.Inventario.model.enums.TipoCuentadante;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;


import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "solicitudes_gil")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class
SolicitudGil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_solicitud", nullable = false,unique = true,length = 50)
    private String codigo;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDate fecha;

    @Column(name = "codigo_regional",nullable = false)
    private Integer codigoRegional = 63;

    @Column(name = "nombre_regional", length = 50, nullable = false)
    private String nombreRegional = "Quindio";

    @Column(name = "codigo_centro_costos", length = 20, nullable = false)
    private String codigoCentroCostos = "953810";

    @Column(name = "jefe_oficina", length = 150)
    private String jefeOficina;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('UNIPERSONAL','MULTIPLE') DEFAULT 'UNIPERSONAL'")
    private TipoCuentadante tipoCuentadante = TipoCuentadante.UNIPERSONAL;


    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = " ENUM('Formación', 'Administrativo') DEFAULT 'Formación'")
    private DestinoBienes destinoBienes = DestinoBienes.FORMACION;


    @Column(name = "ficha_caracterizacion" , length = 50 )
    private String fichaCaracterizacion;


    @OneToOne
    @JoinColumn(name = "pre_factura_id", unique = true)
    private PreFactura preFactura;

    @ManyToOne
    @JoinColumn(name = "factura_global_id")
    private FacturaGlobal facturaGlobal;

    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estado = EstadoSolicitud.PENDIENTE;

    @OneToMany(mappedBy = "solicitudGil", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cuentadante> cuentadantes;

    @OneToMany(mappedBy = "solicitudGil", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SolicitudItems> items;

}
