package com.app.Inventario.controller;


import com.app.Inventario.model.entityMaestras.Cuentadante;
import com.app.Inventario.service.CuentadanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cuentadantes")
public class CuentadanteController {

    private final CuentadanteService cuentadanteService;

    @PostMapping("/{solicitudId}")
    public ResponseEntity<Cuentadante> agregarCuentadante(
            @PathVariable Long solicitudId,
            @RequestBody Cuentadante cuentadante) {
        return ResponseEntity.ok(
                cuentadanteService.agregarCuentadante(solicitudId, cuentadante)
        );
    }

    @PutMapping("/{cuentadanteId}")
    public ResponseEntity<Cuentadante> actualizarCuentadante(
            @PathVariable Long cuentadanteId,
            @RequestBody Cuentadante cuentadante) {
        return ResponseEntity.ok(
                cuentadanteService.actualizarCuentadante(cuentadanteId, cuentadante)
        );
    }

    @DeleteMapping("/{cuentadanteId}")
    public ResponseEntity<Void> eliminarCuentadante(
            @PathVariable Long cuentadanteId) {
        cuentadanteService.eliminarCuentadante(cuentadanteId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/solicitud/{solicitudId}")
    public ResponseEntity<List<Cuentadante>> listarPorSolicitud(
            @PathVariable Long solicitudId) {
        return ResponseEntity.ok(
                cuentadanteService.listarPorSolicitud(solicitudId)
        );
    }
}

