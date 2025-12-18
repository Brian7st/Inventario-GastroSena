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

    private final SolicitudItemsRepository solicitudItemsRepository;
    private final SolicitudGilRepository solicitudGilRepository;

    @Transactional
    public SolicitudItems agregarItems(Long solicitudId, SolicitudItems items) {
        SolicitudGil solicitudGil = solicitudGilRepository.findById(solicitudId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (solicitudGil.getEstado() != EstadoSolicitud.PENDIENTE) {
            throw new RuntimeException("Solo se pueden agregar ítems cuando la solicitud está PENDIENTE");
        }

        // Si manejas lógica de congelación, aquí deberías setear el precio actual del Bien
        if (items.getBien() != null && items.getPrecioCongelado() == null) {
            items.setPrecioCongelado(items.getBien().getValorUnitario());
        }

        items.setSolicitudGil(solicitudGil);
        return solicitudItemsRepository.save(items);
    }

    @Transactional
    public SolicitudItems actualizarItems(Long itemId, SolicitudItems itemsActualizado) {
        SolicitudItems items = solicitudItemsRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Ítem no encontrado"));

        EstadoSolicitud estado = items.getSolicitudGil().getEstado();
        if (estado != EstadoSolicitud.PENDIENTE) {
            throw new RuntimeException("No se pueden modificar ítems de una solicitud en estado: " + estado);
        }

        items.setCantidad(itemsActualizado.getCantidad());
        items.setBien(itemsActualizado.getBien());

        // Actualizar precio congelado si el bien cambió
        items.setPrecioCongelado(itemsActualizado.getBien().getValorUnitario());

        if (items.getCantidad() != null && items.getPrecioCongelado() != null) {
            items.setTotalLinea(items.getCantidad().multiply(items.getPrecioCongelado()));
        }

        return solicitudItemsRepository.save(items);
    }

    @Transactional
    public void eliminarItem(Long itemId) {
        SolicitudItems items = solicitudItemsRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("No se encontró el ítem"));

        EstadoSolicitud estadoSolicitud = items.getSolicitudGil().getEstado();

        if (estadoSolicitud != EstadoSolicitud.PENDIENTE) {
            throw new RuntimeException("No se puede eliminar el ítem de una solicitud ya " + estadoSolicitud);
        }

        solicitudItemsRepository.delete(items);
    }


    @Transactional(readOnly = true)
    public List<SolicitudItems> listarItems(Long solicitudId) {
        return solicitudItemsRepository.findBySolicitudGilIdWithBien(solicitudId);
    }
}