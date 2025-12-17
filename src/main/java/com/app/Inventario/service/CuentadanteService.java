package com.app.Inventario.service;


import com.app.Inventario.model.entity.SolicitudGil;
import com.app.Inventario.model.entityMaestras.Cuentadante;
import com.app.Inventario.model.enums.EstadoSolicitud;
import com.app.Inventario.repository.CuentadanteRepository;
import com.app.Inventario.repository.SolicitudGilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CuentadanteService {

    private final SolicitudGilRepository solicitudGilRepository;
    private final CuentadanteRepository cuentadanteRepository;


    @Transactional
    public Cuentadante agregarCuentadante (Long solicitudId, Cuentadante cuentadante){

        SolicitudGil solicitudGil = solicitudGilRepository.findById(solicitudId)
                .orElseThrow(()-> new RuntimeException("no se encontro la solicitud "));

        if (solicitudGil.getEstado() != EstadoSolicitud.PENDIENTE){

            throw new RuntimeException("Solo se puede asociar con una solicitud en estado pendiente ");
        }
        boolean existe = solicitudGil.getCuentadantes().stream()
                .anyMatch(c -> c.getIdentificacion().equals(cuentadante.getIdentificacion()));

        if (existe) {
            throw new RuntimeException("El cuentadante ya está asociado a la solicitud");
        }

        cuentadante.setSolicitudGil(solicitudGil);
        return cuentadanteRepository.save(cuentadante);
    }

    @Transactional
    public Cuentadante actualizarCuentadante (Long cuentandateId, Cuentadante cuentadanteActualizado){
        Cuentadante cuentadante = cuentadanteRepository.findById(cuentandateId)
                .orElseThrow(()-> new RuntimeException("el cuentadante no encontrado"));

        EstadoSolicitud estadoSolicitud = cuentadante.getSolicitudGil().getEstado();

        if (estadoSolicitud == EstadoSolicitud.APROBADA || estadoSolicitud == EstadoSolicitud.FACTURADA){
            throw new RuntimeException("no se pueden actualizar cuando el estado es APROBADA o FACTURADA");

        }
        cuentadante.setNombre(cuentadanteActualizado.getNombre());
        cuentadante.setIdentificacion(cuentadanteActualizado.getIdentificacion());
        return cuentadanteRepository.save(cuentadante);

    }

    @Transactional
    public void eliminarCuentadante (Long cuentadanteId){
        Cuentadante cuentadante = cuentadanteRepository.findById(cuentadanteId)
                .orElseThrow(() -> new RuntimeException("cuentadante no encontrado"));

        EstadoSolicitud estadoSolicitud = cuentadante.getSolicitudGil().getEstado();

        if (estadoSolicitud == EstadoSolicitud.APROBADA || estadoSolicitud == EstadoSolicitud.FACTURADA){
            throw new RuntimeException("los cuentandate que ya esten asociados a un estado como APROBADO o FACTURADO" +
                    " no se pueden eliminar");

        }
        cuentadanteRepository.delete(cuentadante);
    }

    public List<Cuentadante> listarPorSolicitud(Long solicitudId) {
        return cuentadanteRepository.findBySolicitudGilId(solicitudId);
}}
