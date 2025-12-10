package com.app.Inventario.model.entity;

import com.app.Inventario.model.enums.EstadoPreFactura;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Entity
@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
public class PreFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String numero;

    @Column(name = "fecha", updatable = false)
    private LocalDateTime fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programa_id", referencedColumnName = "id")
    private ProgamaFormacion progamaFormacion;
    @Column(name ="instructor_nombre", length = 150)
    private String nombreInstructor;

    @Column(name = "instructor_identificacion", length = 20)
    private String identificacionInstructor;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('PENDIENTE','VALIDADA','ANULADA')")
    private EstadoPreFactura estado;

    @Column(name = "total_general", precision = 15, scale = 2)
    private BigDecimal totalPrefactura;


}
