package com.app.Inventario.controller;

import com.app.Inventario.model.dto.request.RegistroMovimientoDTO;
import com.app.Inventario.model.dto.response.MovimientoResponseDTO;
import com.app.Inventario.service.MovimientoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses; // Import añadido para ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
@Tag(name = "Gestión de Movimientos", description = "Endpoints para registrar entradas, salidas y consultar historial de inventario")
public class MovimientoController {

    private final MovimientoService movimientoService;

    @PostMapping("/entrada")
    @Operation(
            summary = "Registrar Entradas de Stock (Manual)",
            description = "Incrementa el stock de uno o varios bienes. Genera historial y actualiza el estado del bien.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Entrada registrada exitosamente",
                            content = @Content(schema = @Schema(implementation = MovimientoResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Bien no encontrado"),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud")
            }
    )
    public ResponseEntity<List<MovimientoResponseDTO>> registrarEntrada(
            @Valid @RequestBody RegistroMovimientoDTO request) {
        return ResponseEntity.ok(movimientoService.registrarEntrada(request));
    }

    @PostMapping("/salida")
    @Operation(
            summary = "Registrar Salidas de Stock",
            description = "Descarga stock de inventario. Valida disponibilidad antes de procesar.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Salida registrada exitosamente"),
                    @ApiResponse(responseCode = "409", description = "Stock insuficiente para realizar la operación"),
                    @ApiResponse(responseCode = "404", description = "Bien no encontrado")
            }
    )
    public ResponseEntity<List<MovimientoResponseDTO>> registrarSalida(
            @Valid @RequestBody RegistroMovimientoDTO request) {
        return ResponseEntity.ok(movimientoService.registrarSalida(request));
    }

    // NUEVO ENDPOINT PARA LA CARGA MASIVA (RF-5.5.2)
    @PostMapping("/entrada/gil-solicitud/{solicitudGilId}")
    @Operation(
            summary = "Carga Masiva de Bienes desde Solicitud GIL",
            description = "Procesa una Solicitud GIL APROBADA, registra entradas de stock para todos sus ítems y marca la solicitud como PROCESADA.",
            parameters = @Parameter(name = "solicitudGilId", description = "ID de la Solicitud GIL (GIL-F-014) APROBADA", required = true)
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carga masiva completada exitosamente",
                    content = @Content(schema = @Schema(implementation = MovimientoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Solicitud GIL o Bien no encontrado"),
            @ApiResponse(responseCode = "400", description = "La solicitud no está APROBADA o ya fue procesada/utilizada")
    })
    public ResponseEntity<List<MovimientoResponseDTO>> registrarEntradaMasivaGil(
            @PathVariable Long solicitudGilId) {
        return ResponseEntity.ok(movimientoService.registrarEntradaMasivaGil(solicitudGilId));
    }

    @GetMapping("/historial/{bienId}")
    @Operation(
            summary = "Consultar Historial por Bien",
            description = "Obtiene la lista de todos los movimientos (entradas y salidas) de un producto específico, ordenados por fecha descendente.",
            parameters = @Parameter(name = "bienId", description = "ID del bien a consultar", required = true)
    )
    public ResponseEntity<List<MovimientoResponseDTO>> obtenerHistorial(@PathVariable Long bienId) {
        return ResponseEntity.ok(movimientoService.obtenerHistorialPorBien(bienId));
    }
}