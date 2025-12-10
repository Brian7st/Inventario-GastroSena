package com.app.Inventario.repository;

import com.app.Inventario.model.entity.Bien;
import com.app.Inventario.model.enums.EstadoBien;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BienRepository extends JpaRepository<Bien, Long> {
    Optional<Bien> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    List<Bien> findByCategoria_id(Long categoriaId);

    List<Bien> findByEstado(EstadoBien estado);

    List<Bien> findByNombreContainingIgnoreCase(String nombre);
}
