package com.app.Inventario.repository;

import com.app.Inventario.model.entity.FacturaGlobal;
import com.app.Inventario.model.entity.SolicitudGil;
import com.app.Inventario.model.enums.EstadoSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SolicitudGilRepository extends JpaRepository<SolicitudGil, Long> {


    @Query("SELECT DISTINCT s FROM SolicitudGil s LEFT JOIN FETCH s.items i LEFT JOIN FETCH i.bien LEFT JOIN FETCH s.cuentadantes LEFT JOIN FETCH s.preFactura WHERE s.id = :id")
    Optional<SolicitudGil> findFullById(@Param("id") Long id);

    @Query("SELECT DISTINCT s FROM SolicitudGil s LEFT JOIN FETCH s.items i LEFT JOIN FETCH i.bien LEFT JOIN FETCH s.cuentadantes LEFT JOIN FETCH s.preFactura")
    List<SolicitudGil> findAllFull();




    List<SolicitudGil> findByIdInAndFacturaGlobalIsNotNull(List<Long> ids);


    List<SolicitudGil> findByIdInAndEstadoAndFacturaGlobalIsNull(List<Long> ids, EstadoSolicitud estado);

    List<SolicitudGil> findByFacturaGlobal(FacturaGlobal facturaGlobal);
}
