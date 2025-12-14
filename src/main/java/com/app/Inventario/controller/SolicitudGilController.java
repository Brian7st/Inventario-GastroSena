package com.app.Inventario.controller;

import com.app.Inventario.model.dto.SolicitudGilResponseDTO;
import com.app.Inventario.model.entity.SolicitudGil;
import com.app.Inventario.service.SolicitudGilService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/solicitudes-gil")
@RequiredArgsConstructor

public class SolicitudGilController {

    private final SolicitudGilService solicitudGilService;

    @PostMapping
    public ResponseEntity<SolicitudGilResponseDTO> crearSolicitud(
            @RequestBody SolicitudGil solicitudGil) {
        return ResponseEntity.ok(
                solicitudGilService.crearSolicitud(solicitudGil)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudGilResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(
                solicitudGilService.obtenerId(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<SolicitudGilResponseDTO>> listarSolicitudes() {
        return ResponseEntity.ok(
                solicitudGilService.obtenerSolicitudes()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitudGilResponseDTO> actualizarSolicitud(
            @PathVariable Long id,
            @RequestBody SolicitudGil solicitudGil) {
        return ResponseEntity.ok(
                solicitudGilService.actualizarSolicitud(id, solicitudGil)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSolicitud(@PathVariable Long id) {
        solicitudGilService.BorrarSolicitud(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{solicitudId}/prefactura/{preFacturaId}")
    public ResponseEntity<SolicitudGilResponseDTO> asociarPreFactura(
            @PathVariable Long solicitudId,
            @PathVariable Long preFacturaId) {
        return ResponseEntity.ok(
                solicitudGilService.asociarPreFactura(solicitudId, preFacturaId)
        );
    }

}
