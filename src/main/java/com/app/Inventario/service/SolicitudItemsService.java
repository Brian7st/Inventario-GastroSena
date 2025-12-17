package com.app.Inventario.service;

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
public class SolicitudItemsService {



    private  final SolicitudItemsRepository solicitudItemsRepository;
    private final  SolicitudGilRepository solicitudGilRepository;


    @Transactional
    public SolicitudItems agregarItems (Long solicitudId, SolicitudItems items){
        SolicitudGil solicitudGil = solicitudGilRepository.findById(solicitudId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encotrada"));

        if (solicitudGil.getEstado() != EstadoSolicitud.PENDIENTE) {
            throw new RuntimeException("Solo se pueden agregar items cuando la solicitud está PENDIENTE");
        }

        items.setSolicitudGil(solicitudGil);
        return solicitudItemsRepository.save(items);
    }

    @Transactional
    public SolicitudItems actualizarItems (Long itemId,SolicitudItems itemsActualizado ){
        SolicitudItems items = solicitudItemsRepository.findById(itemId)
                .orElseThrow(()-> new RuntimeException("item no encontrado"));


        if (items.getSolicitudGil().getEstado() == EstadoSolicitud.FACTURADA) {
            throw new RuntimeException("No se pueden modificar items de una solicitud facturada");
        }

        items.setCantidad(itemsActualizado.getCantidad());
        items.setBien(itemsActualizado.getBien());

        return solicitudItemsRepository.save(items);

    }

    @Transactional
    public void eliminarItem (Long itemId){

        SolicitudItems items = solicitudItemsRepository.findById(itemId)
                        .orElseThrow(()-> new RuntimeException("no se encontro item"));
        EstadoSolicitud estadoSolicitud = items.getSolicitudGil().getEstado();

        if (estadoSolicitud == EstadoSolicitud.FACTURADA || estadoSolicitud == EstadoSolicitud.APROBADA){

       throw new RuntimeException("no s epuede eliminar el item de una solicitud aporabado o facturada");

    }   solicitudItemsRepository.deleteById(itemId);

    }

    public List<SolicitudItems> listarItems (Long solicitudId){

        return solicitudItemsRepository.findBySolicitudGilId(solicitudId);

        }
    }



