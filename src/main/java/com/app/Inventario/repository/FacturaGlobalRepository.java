package com.app.Inventario.repository;

import com.app.Inventario.model.entity.FacturaGlobal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FacturaGlobalRepository extends JpaRepository<FacturaGlobal, Long> {

    Optional<FacturaGlobal> findTopByOrderByNumeroConsecutivoDesc();
}