package com.app.Inventario.model.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "bienes")
@Data // Genera getters, setters, toString, equals, hashcode
@NoArgsConstructor // Constructor vacío requerido por JPA
@AllArgsConstructor // Constructor con todos los argumentos
public class Bien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(nullable = false, length = 200)
    private String nombre;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(name = "unidad_medida", nullable = false, length = 20)
    private String unidadMedida;

    @Column(name = "valor_unitario", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorUnitario = BigDecimal.ZERO; // Valor por defecto

    @Column(name = "porcentaje_iva", precision = 5, scale = 2)
    private BigDecimal porcentajeIva = BigDecimal.ZERO; // Valor por defecto


    @Column(name = "valor_con_iva", insertable = false, updatable = false, precision = 15, scale = 2)
    private BigDecimal valorConIva;

    @Column(name = "stock_actual", precision = 15, scale = 2)
    private BigDecimal stockActual = BigDecimal.ZERO;

    @Column(name = "stock_minimo", precision = 15, scale = 2)
    private BigDecimal stockMinimo = new BigDecimal("5.00");

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('DISPONIBLE','BAJO STOCK','SIN STOCK','ELIMINADO') DEFAULT 'DISPONIBLE'")
    private EstadoBien estado = EstadoBien.DISPONIBLE;
}
