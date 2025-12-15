package com.app.Inventario.model.entity;


import com.app.Inventario.model.entityMaestras.Categoria;
import com.app.Inventario.model.entityMaestras.Impuesto;
import com.app.Inventario.model.entityMaestras.UnidadMedida;
import com.app.Inventario.model.enums.EstadoBien;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bienes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bien  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(name = "cod_almacen", length = 50)
    private String codAlmacen;

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_unidad", nullable = false)
    private UnidadMedida unidadMedida;

    @Column(name = "valor_unitario", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorUnitario = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_impuesto", nullable = false)
    private Impuesto impuesto;


    @Column(name = "stock_actual", precision = 15, scale = 2)
    private BigDecimal stockActual = BigDecimal.ZERO;

    @Column(name = "stock_minimo", precision = 15, scale = 2)
    private BigDecimal stockMinimo = BigDecimal.ZERO;

    @Column(name = "valor_total_stock", insertable = false, updatable = false, precision = 19, scale = 2)
    private BigDecimal valorTotalStock;

    @Column(nullable = false)
    private Boolean activo;

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('DISPONIBLE','BAJO STOCK','SIN STOCK','ELIMINADO') DEFAULT 'DISPONIBLE'")
    private EstadoBien estado = EstadoBien.DISPONIBLE;
}
