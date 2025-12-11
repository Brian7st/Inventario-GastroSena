package com.app.Inventario.model.entity;


import com.app.Inventario.model.enums.EstadoPrograma;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProgramaFormacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", length = 150)
    private String nombre;

    @Column(name = "codigo_ficha", length = 50)
    private String codigoFicha;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('ACTIVO', 'INACTIVO')")
    private EstadoPrograma estadoPrograma;

    @OneToMany(mappedBy = "programa", fetch = FetchType.LAZY)
    private List<PreFactura> preFacturas;

}
