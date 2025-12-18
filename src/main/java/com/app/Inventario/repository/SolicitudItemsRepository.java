package com.app.Inventario.repository;

import com.app.Inventario.model.entity.SolicitudItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface SolicitudItemsRepository extends JpaRepository<SolicitudItems, Long> {

    // Útil para saber qué bienes se pidieron en un grupo de solicitudes
    List<SolicitudItems> findBySolicitudGil_IdIn(List<Long> solicitudGilIds);

    // Útil para auditoría: ¿En qué solicitudes aparece este Bien específico?
    @Query("SELECT si FROM SolicitudItems si WHERE si.bien.id = :bienId")
    List<SolicitudItems> findAllByBienId(@Param("bienId") Long bienId);

    @Query("SELECT si FROM SolicitudItems si JOIN FETCH si.bien WHERE si.solicitudGil.id = :solicitudId")
    List<SolicitudItems> findBySolicitudGilIdWithBien(@Param("solicitudId") Long solicitudId);

    @Query("SELECT SUM(si.cantidad) FROM SolicitudItems si " +
            "WHERE si.bien.id = :bienId " +
            "AND si.solicitudGil.fecha BETWEEN :inicio AND :fin")
    BigDecimal sumCantidadByBienAndFechas(
            @Param("bienId") Long bienId,
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin
    );

}
