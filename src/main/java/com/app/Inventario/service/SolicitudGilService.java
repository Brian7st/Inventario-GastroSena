package com.app.Inventario.service;

import com.app.Inventario.mapper.SolicitudGilMapper;
import com.app.Inventario.model.dto.request.SolicitudGilRequestDTO;
import com.app.Inventario.model.dto.response.SolicitudGilResponseDTO;
import com.app.Inventario.model.entity.PreFactura;
import com.app.Inventario.model.entity.SolicitudGil;
import com.app.Inventario.model.entity.SolicitudItems;
import com.app.Inventario.model.enums.EstadoPreFactura;
import com.app.Inventario.model.enums.EstadoSolicitud;
import com.app.Inventario.repository.SolicitudGilRepository;
import com.app.Inventario.repository.SolicitudItemsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SolicitudGilService {
    private final SolicitudGilRepository solicitudGilRepository;
    private final SolicitudItemsRepository solicitudItemsRepository;
    private final SolicitudGilMapper solicitudGilMapper;
    private final PreFacturaService preFacturaService;

    @Transactional
    public SolicitudGilResponseDTO crearSolicitud(SolicitudGilRequestDTO dto){
        SolicitudGil solicitudGil = solicitudGilMapper.toEntity(dto);

        SolicitudGil saved = solicitudGilRepository.save(solicitudGil);
        return solicitudGilMapper.toResponseDto(saved);
    }

    public SolicitudGilResponseDTO obtenerId(Long id){
        SolicitudGil solicitudGil = solicitudGilRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no Encontrada Por id: " + id));
        return solicitudGilMapper.toResponseDto(solicitudGil);
    }

    @Transactional
    public SolicitudGilResponseDTO actualizarSolicitud(Long id, SolicitudGilRequestDTO dto) {
        SolicitudGil solicitud = solicitudGilRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada por id: " + id));

        if (solicitud.getEstado() != EstadoSolicitud.PENDIENTE) {
            throw new RuntimeException("Solo se puede modificar una solicitud en estado PENDIENTE");
        }

        solicitudGilMapper.updateEntityFromDto(solicitud, dto);

        return solicitudGilMapper.toResponseDto(
                solicitudGilRepository.save(solicitud)
        );
    }

    public List<SolicitudGilResponseDTO> obtenerSolicitudes (){
        List<SolicitudGil> solicitudes = solicitudGilRepository.findAll();
        return solicitudGilMapper.toResponseDtos(solicitudes);
    }

    @Transactional
    public void borrarSolicitud(Long id) {
        SolicitudGil solicitud = solicitudGilRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada por id: " + id));

        if (solicitud.getEstado() != EstadoSolicitud.PENDIENTE) {
            throw new RuntimeException("Solo se pueden eliminar solicitudes en estado PENDIENTE");
        }

        solicitudGilRepository.delete(solicitud);
    }

    @Transactional
    public SolicitudGilResponseDTO asociarPreFactura (Long solicitudId, Long preFacturaId){
        SolicitudGil solicitudGil = solicitudGilRepository.findById(solicitudId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada por el Id: " + solicitudId));

        if (solicitudGil.getPreFactura() != null) {
            throw new RuntimeException("Esta Solicitud ya tiene una PreFactura asociada.");
        }

        if (solicitudGil.getEstado() != EstadoSolicitud.PENDIENTE) {
            throw new RuntimeException("Solo se pueden asociar prefacturas a solicitudes PENDIENTES.");
        }

        PreFactura preFactura = preFacturaService.obtenerEntidadPorId(preFacturaId);

        if (preFactura.getEstado() != EstadoPreFactura.VALIDADA) {
            throw new RuntimeException("Solo se pueden asociar prefacturas en estado VALIDADA.");
        }

        solicitudGil.setPreFactura(preFactura);

        List<SolicitudItems> nuevosItems = preFactura.getDetalles().stream()
                .map(detalle -> {
                    SolicitudItems item = new SolicitudItems();
                    item.setSolicitudGil(solicitudGil);
                    item.setBien(detalle.getBien());
                    item.setCantidad(detalle.getCantidad());
                    return item;
                })
                .collect(Collectors.toList());

        solicitudItemsRepository.saveAll(nuevosItems);

        SolicitudGil solicitudGuardada = solicitudGilRepository.save(solicitudGil);
        return solicitudGilMapper.toResponseDto(solicitudGuardada);
    }
    @Transactional
    public SolicitudGilResponseDTO actualizarEstado(Long id, EstadoSolicitud nuevoEstado) {
        SolicitudGil solicitud = solicitudGilRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada por id: " + id));

        EstadoSolicitud estadoActual = solicitud.getEstado();


        if (estadoActual == EstadoSolicitud.APROBADA || estadoActual == EstadoSolicitud.RECHAZADA) {
            throw new RuntimeException("No se puede modificar el estado de una solicitud ya APROBADA o RECHAZADA.");
        }

        if (estadoActual == EstadoSolicitud.PENDIENTE && nuevoEstado == EstadoSolicitud.PROCESADA) {
            throw new RuntimeException("Una solicitud solo puede pasar a PROCESADA después de asociar una PreFactura (usar el método 'asociarPreFactura').");
        }


        solicitud.setEstado(nuevoEstado);



        SolicitudGil updated = solicitudGilRepository.save(solicitud);
        return solicitudGilMapper.toResponseDto(updated);
    }
    public SolicitudGil obtenerEntidadPorId(Long id) {
        return solicitudGilRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud GIL no encontrada por id: " + id));
    }
}
