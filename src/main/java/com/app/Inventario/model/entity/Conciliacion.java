package com.app.Inventario.model.entity;

import com.app.Inventario.model.enums.EstadoConciliacion;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "conciliaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Conciliacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_corte", nullable = false)
    private LocalDate fechaCorte;

    @Column(name = "responsable", length = 150)
    private String responsable;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('ABIERTA','CERRADA')")
    private EstadoConciliacion estado;

    @Column(name = "total_diferencia_valor", precision = 15, scale = 2)
    private BigDecimal totalDiferenciaValor = BigDecimal.ZERO;

    @ToString.Exclude
    @OneToMany(mappedBy = "conciliacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConciliacionDetalle> detalles;
}