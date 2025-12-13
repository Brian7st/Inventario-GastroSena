package com.app.Inventario.repository;

import com.app.Inventario.model.entity.PreFacturaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreFacturaDetalleRepository extends JpaRepository<PreFacturaDetalle, Long> {

}