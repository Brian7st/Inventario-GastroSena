package com.app.Inventario.repository;

import com.app.Inventario.model.entity.SolicitudItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitudItemsRepository extends JpaRepository<SolicitudItems, Long> {

    List<SolicitudItems> findBySolicitudGilId (Long solicitudId);
}
