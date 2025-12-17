package com.app.Inventario.repository;

import com.app.Inventario.model.entity.Conciliacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.Optional;

public interface ConciliacionRepository extends JpaRepository<Conciliacion, Long> {

    Optional<Conciliacion> findByFechaCorteAndEstado(LocalDate fechaCorte, String estado);

    Optional<Conciliacion> findTopByOrderByFechaCorteDesc();
}