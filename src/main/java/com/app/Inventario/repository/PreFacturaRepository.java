package com.app.Inventario.repository;

import com.app.Inventario.model.entity.PreFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PreFacturaRepository extends JpaRepository<PreFactura, Long> {

    boolean existsByNumero(String numero);
    @Query("SELECT p FROM PreFactura p LEFT JOIN FETCH p.programaFormacion LEFT JOIN FETCH p.detalles WHERE p.id = :id")
    Optional<PreFactura> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT p FROM PreFactura p LEFT JOIN FETCH p.programaFormacion")
    List<PreFactura> findAllWithPrograma();

    @Query("""
    SELECT DISTINCT pf
    FROM PreFactura pf
    LEFT JOIN FETCH pf.detalles d
    LEFT JOIN FETCH d.bien
    WHERE pf.id = :id
""")
    Optional<PreFactura> findByIdFull(@Param("id") Long id);
}

