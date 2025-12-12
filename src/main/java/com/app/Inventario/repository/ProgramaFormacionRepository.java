package com.app.Inventario.repository;

import com.app.Inventario.model.entity.ProgramaFormacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramaFormacionRepository extends JpaRepository<ProgramaFormacion, Long> {

}