package com.app.Inventario.controller;

import com.app.Inventario.model.entity.SolicitudItems;
import com.app.Inventario.service.SolicitudItemsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes-gil/items")
@RequiredArgsConstructor
public class SolicitudItemsController {


    private final SolicitudItemsService solicitudItemsService;

    @PostMapping("/{solicitudId}")
    public ResponseEntity<SolicitudItems> agregarItem(
            @PathVariable Long solicitudId,
            @RequestBody SolicitudItems item) {
        return ResponseEntity.ok(
                solicitudItemsService.agregarItems(solicitudId, item)
        );
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<SolicitudItems> actualizarItem(
            @PathVariable Long itemId,
            @RequestBody SolicitudItems item) {
        return ResponseEntity.ok(
                solicitudItemsService.actualizarItems(itemId, item)
        );
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> eliminarItem(@PathVariable Long itemId) {
        solicitudItemsService.eliminarItem(itemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/solicitud/{solicitudId}")
    public ResponseEntity<List<SolicitudItems>> listarItemsPorSolicitud(
            @PathVariable Long solicitudId) {
        return ResponseEntity.ok(
                solicitudItemsService.listarItems(solicitudId)
        );
    }
}
