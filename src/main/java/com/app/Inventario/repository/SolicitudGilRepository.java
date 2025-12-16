package com.app.Inventario.repository;

import com.app.Inventario.model.entity.FacturaGlobal;
import com.app.Inventario.model.entity.SolicitudGil;
import com.app.Inventario.model.enums.EstadoSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SolicitudGilRepository extends JpaRepository<SolicitudGil, Long> {

    List<SolicitudGil>findByEstado(EstadoSolicitud estadoSolicitud);

    List<SolicitudGil>findByCodigoRegional(Integer codigoRegional);

    List<SolicitudGil>findByFechaBetween(LocalDate inicio, LocalDate fin);

    List<SolicitudGil> findByIdInAndFacturaGlobalIsNotNull(List<Long> ids);
    
    List<SolicitudGil> findByIdInAndEstadoAndFacturaGlobalIsNull(List<Long> ids, EstadoSolicitud estado);
    
    List<SolicitudGil> findByFacturaGlobal(FacturaGlobal facturaGlobal);
}
