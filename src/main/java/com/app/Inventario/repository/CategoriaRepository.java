package com.app.Inventario.repository;

import com.app.Inventario.model.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findByActivoTrue();

    boolean existsByNombre(String nombre);

    Optional<Categoria> findByNombre(String nombre);
}
