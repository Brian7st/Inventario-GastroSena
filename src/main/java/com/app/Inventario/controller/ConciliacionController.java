package com.app.Inventario.controller;

import com.app.Inventario.model.dto.request.ConciliacionRequestDTO;
import com.app.Inventario.model.dto.response.ConciliacionDetalleResponseDTO;
import com.app.Inventario.model.dto.response.ConciliacionResponseDTO;
import com.app.Inventario.service.ConciliacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/conciliaciones")
@RequiredArgsConstructor
@Tag(name = "Gestión de Conciliaciones de Inventario")
public class ConciliacionController {

    private final ConciliacionService conciliacionService;

    @Operation(summary = "Iniciar un proceso de conciliación")
    @PostMapping
    public ResponseEntity<ConciliacionResponseDTO> iniciarConciliacion(
            @Valid @RequestBody ConciliacionRequestDTO request) {

        ConciliacionResponseDTO response = conciliacionService.iniciarConciliacion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Obtener detalles de una Conciliación por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ConciliacionResponseDTO> obtenerConciliacion(@PathVariable Long id) {

        ConciliacionResponseDTO response = conciliacionService.obtenerPorId(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "3. Justificar la diferencia en una línea de detalle")
    @PutMapping("/detalles/{detalleId}/justificar")
    public ResponseEntity<ConciliacionDetalleResponseDTO> justificarDiferencia(
            @PathVariable Long detalleId,
            @RequestParam String justificacion) {

        ConciliacionDetalleResponseDTO response = conciliacionService.justificarDiferencia(detalleId, justificacion);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "4. Aplicar Ajuste de Inventario y Cerrar la Conciliación (Acción Final)")
    @PostMapping("/{id}/ajustar")
    public ResponseEntity<ConciliacionResponseDTO> aplicarAjusteYCerrar(@PathVariable Long id) {

        ConciliacionResponseDTO response = conciliacionService.aplicarAjusteYCerrar(id);
        return ResponseEntity.ok(response);
    }
}