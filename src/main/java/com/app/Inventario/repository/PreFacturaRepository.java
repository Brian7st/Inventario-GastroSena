package com.app.Inventario.repository;

import com.app.Inventario.model.entity.PreFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreFacturaRepository extends JpaRepository<PreFactura, Long> {

    // Spring Data crea este método automáticamente
    boolean existsByNumero(String numero);

}

