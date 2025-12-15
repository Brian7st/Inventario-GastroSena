package com.app.Inventario.repository;

import com.app.Inventario.model.entityMaestras.UnidadMedida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UnidadMedidaRepository extends JpaRepository<UnidadMedida, Integer> {
    boolean existsByNombre(String nombre);
    boolean existsByAbreviatura(String abreviatura);
}
