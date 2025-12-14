package com.app.Inventario.service;

import com.app.Inventario.mapper.SolicitudGilMapper;
import com.app.Inventario.mapper.SolicitudItemMapper;
import com.app.Inventario.model.dto.SolicitudGilResponseDTO;
import com.app.Inventario.model.entity.PreFactura;
import com.app.Inventario.model.entity.SolicitudGil;
import com.app.Inventario.model.entity.SolicitudItems;
import com.app.Inventario.model.enums.EstadoSolicitud;
import com.app.Inventario.repository.SolicitudGilRepository;
import com.app.Inventario.repository.SolicitudItemsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitudGilService {
    private final SolicitudGilRepository solicitudGilRepository;
    private final SolicitudGilMapper solicitudGilMapper;
    private final PreFacturaService preFacturaService;



    @Transactional
    public SolicitudGilResponseDTO crearSolicitud(@org.jetbrains.annotations.NotNull SolicitudGil solicitudGil){
        solicitudGil.setEstado(EstadoSolicitud.PENDIENTE);
        SolicitudGil saved = solicitudGilRepository.save(solicitudGil);
        return solicitudGilMapper.toResponseDto(saved);
    }


    public SolicitudGilResponseDTO obtenerId(Long id){
        SolicitudGil solicitudGil = solicitudGilRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no Encontrada Por id" + id));
                        return solicitudGilMapper.toResponseDto(solicitudGil);


    }


    @Transactional
    public SolicitudGilResponseDTO actualizarSolicitud(Long id, SolicitudGil solicitudActualizada) {
        SolicitudGil solicitud = solicitudGilRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (solicitud.getEstado() != EstadoSolicitud.PENDIENTE) {
            throw new RuntimeException("Solo se puede modificar una solicitud en estado PENDIENTE");
        }

        // SOLO campos propios de la solicitud
        solicitud.setEstado(solicitudActualizada.getEstado());
        solicitud.setItems(solicitudActualizada.getItems());

        return solicitudGilMapper.toResponseDto(
                solicitudGilRepository.save(solicitud)
        );
    }


    public List<SolicitudGilResponseDTO> obtenerSolicitudes (){
        List<SolicitudGil> solicitudes = solicitudGilRepository.findAll();
        return solicitudGilMapper.toResponseDtos(solicitudes);
    }

    @Transactional
    public void BorrarSolicitud(Long id) {
        SolicitudGil solicitud = solicitudGilRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (solicitud.getEstado() != EstadoSolicitud.PENDIENTE) {
            throw new RuntimeException("Solo se pueden eliminar solicitudes en estado PENDIENTE");
        }

        solicitudGilRepository.delete(solicitud);
    }





    @Transactional
    public SolicitudGilResponseDTO asociarPreFactura (Long solicitudId, Long preFacturaId){
        SolicitudGil solicitudGil = solicitudGilRepository.findById(solicitudId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada por el Id" + solicitudId));

        if (solicitudGil.getEstado() != EstadoSolicitud.VALIDADA) {
            throw new RuntimeException("solo se pueden asociar prefacturas con solicitudes ya validadas");

        }
        PreFactura preFactura = preFacturaService.obtenerPorId(preFacturaId);


        solicitudGil.setPreFactura(preFactura);

        solicitudGil.setEstado(EstadoSolicitud.FACTURADA);


        SolicitudGil solicitudGuardada = solicitudGilRepository.save(solicitudGil);
        return SolicitudGilMapper.toResponseDto(solicitudGuardada);



    }


}
