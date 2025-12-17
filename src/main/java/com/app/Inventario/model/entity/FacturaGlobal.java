package com.app.Inventario.model.entity;

import com.app.Inventario.model.enums.EstadoFacturaGlobal;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "facturas_globales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacturaGlobal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_consecutivo", nullable = false, unique = true, length = 50)
    private String numeroConsecutivo;

    @Column(name = "fecha_generacion")
    private LocalDateTime fechaGeneracion;

    @Column(name = "total_final", precision = 15, scale = 2)
    private BigDecimal totalFinal;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('GENERADA','PAGADA')")
    private EstadoFacturaGlobal estado;

    @ToString.Exclude
    @OneToMany(mappedBy = "facturaGlobal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FacturaGlobalDetalle> detalles;
}