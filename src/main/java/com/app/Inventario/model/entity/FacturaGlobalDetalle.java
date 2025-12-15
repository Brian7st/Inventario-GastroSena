package com.app.Inventario.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Table(name = "factura_global_detalles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacturaGlobalDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factura_global_id", nullable = false)
    private FacturaGlobal facturaGlobal; 
    @Column(name = "codigo_producto", length = 50, nullable = false)
    private String codigoProducto;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "cantidad", precision = 15, scale = 2)
    private BigDecimal cantidad;

    @Column(name = "valor_unitario_sin_iva", precision = 15, scale = 2)
    private BigDecimal valorUnitarioSinIva;

    @Column(name = "iva_porcentaje", precision = 5, scale = 2)
    private BigDecimal ivaPorcentaje;

    @Column(name = "total_sin_iva", precision = 15, scale = 2)
    private BigDecimal totalSinIva;

    @Column(name = "valor_iva", precision = 15, scale = 2)
    private BigDecimal valorIva;

    @Column(name = "total_con_iva", precision = 15, scale = 2)
    private BigDecimal totalConIva;
}