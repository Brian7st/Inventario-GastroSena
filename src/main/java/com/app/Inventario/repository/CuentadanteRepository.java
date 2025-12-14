package com.app.Inventario.repository;

import com.app.Inventario.model.entity.Cuentadante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CuentadanteRepository extends JpaRepository<Cuentadante, Long> {


    List<Cuentadante> findBySolicitudId (Long solicitudGil);
}
