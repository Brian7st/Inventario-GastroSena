package com.app.Inventario.repository;

import com.app.Inventario.model.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findByBienIdOrderByFechaMovimientoDesc(Long bienId);
}