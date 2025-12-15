package com.app.Inventario.repository;

import com.app.Inventario.model.entityMaestras.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    List<Categoria> findByActivoTrue();

    boolean existsByNombre(String nombre);

    Optional<Categoria> findByNombre(String nombre);
}
