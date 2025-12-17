package com.app.Inventario.repository;

import com.app.Inventario.model.entity.Conciliacion;
import com.app.Inventario.model.entity.ConciliacionDetalle;
import com.app.Inventario.model.enums.EstadoLinea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConciliacionDetalleRepository extends JpaRepository<ConciliacionDetalle, Long> {

    List<ConciliacionDetalle> findByConciliacion(Conciliacion conciliacion);

    List<ConciliacionDetalle> findByConciliacionAndEstadoLinea(Conciliacion conciliacion, EstadoLinea estadoLinea);
}