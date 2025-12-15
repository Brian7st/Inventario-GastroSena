package com.app.Inventario.repository;

import com.app.Inventario.model.entityMaestras.Impuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImpuestoRepository extends JpaRepository<Impuesto, Integer> {
    boolean existsByNombre(String nombre);
}
