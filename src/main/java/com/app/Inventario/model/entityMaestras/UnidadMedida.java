package com.app.Inventario.model.entityMaestras;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "unidad_medida")
public class UnidadMedida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_unidad")
    private Integer idUnidad;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 10)
    private String abreviatura;

    @Column(name = "codigo_fiscal", length = 20)
    private String codigoFiscal;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;
}
