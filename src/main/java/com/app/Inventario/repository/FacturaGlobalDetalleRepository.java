package com.app.Inventario.repository;

import com.app.Inventario.model.entity.FacturaGlobalDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacturaGlobalDetalleRepository extends JpaRepository<FacturaGlobalDetalle, Long> {
}