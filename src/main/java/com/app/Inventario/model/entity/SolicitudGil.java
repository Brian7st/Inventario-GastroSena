package com.app.Inventario.model.entity;

import com.app.Inventario.model.enums.DestinoBienes;
import com.app.Inventario.model.enums.TipoCuentadante;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;


import java.time.LocalDate;

@Entity
@Table(name = "solicitudes_gil")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudGil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigoSolicitud", nullable = false,unique = true,length = 50)
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

    @Column(name = "jefe_oficina", length = 150, nullable = false)
    private String jefeOficina;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('UNIPERSONAL','MULTIPLE') DEFAULT 'UNIPERSONAL'")
    private TipoCuentadante tipoCuentadante = TipoCuentadante.UNIPERSONAL;


    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = " ENUM('Formación', 'Administrativo') DEFAULT 'Formación'")
    private DestinoBienes destinoBienes = DestinoBienes.FORMACION;


    @Column(name = "ficha_caracterizacion" , length = 50 , nullable = false)
    private String fichaCaracterizacion;


}
