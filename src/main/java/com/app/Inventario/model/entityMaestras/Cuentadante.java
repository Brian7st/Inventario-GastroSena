package com.app.Inventario.model.entityMaestras;

import com.app.Inventario.model.entity.SolicitudGil;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table (name = "gil_cuentadantes")
@Data
public class Cuentadante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @Column(name = "nombre", length = 150)
    private String nombre;


    @Column(name = "identificacion", length = 20)
    private String identificacion;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_gil_id", nullable = false)
    private SolicitudGil solicitudGil;
}
