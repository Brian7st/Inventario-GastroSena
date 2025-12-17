package com.app.Inventario.model.entity;

import com.app.Inventario.model.enums.EstadoLinea;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "conciliacion_detalle")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConciliacionDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conciliacion_id", nullable = false)
    private Conciliacion conciliacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bien_id", nullable = false)
    private Bien bien;

    @Column(name = "conteo_sistema", precision = 15, scale = 2, nullable = false)
    private BigDecimal conteoSistema;

    @Column(name = "conteo_fisico", precision = 15, scale = 2, nullable = false)
    private BigDecimal conteoFisico;

    @Column(name = "diferencia_cantidad", precision = 15, scale = 2)
    private BigDecimal diferenciaCantidad;

    @Column(name = "valor_diferencia", precision = 15, scale = 2)
    private BigDecimal valorDiferencia;

    @Enumerated(EnumType.STRING)
    private EstadoLinea estadoLinea = EstadoLinea.PENDIENTE;

    @Column(name = "justificacion", columnDefinition = "TEXT")
    private String justificacion;

}