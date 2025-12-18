package com.app.Inventario.controller;

import com.app.Inventario.model.dto.EstadoSolicitudUpdateDTO;
import com.app.Inventario.model.dto.request.SolicitudGilRequestDTO;
import com.app.Inventario.model.dto.response.SolicitudGilResponseDTO;
import com.app.Inventario.service.SolicitudGilService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes-gil")
@RequiredArgsConstructor
@Tag(name = "Solicitudes GIL", description = "Gestión de Solicitudes de Gestión de Inventarios y Logística.")
public class SolicitudGilController {

    private final SolicitudGilService solicitudGilService;

    @Operation(
            summary = "Crear nueva solicitud desde Pre-Factura",
            description = "Registra una solicitud GIL vinculada a una Pre-Factura validada. Clona automáticamente los ítems de la Pre-Factura."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Solicitud creada y bienes asociados exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SolicitudGilResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Pre-Factura no válida o datos de entrada incorrectos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<SolicitudGilResponseDTO> crearSolicitud(@RequestBody SolicitudGilRequestDTO dto) {
        return new ResponseEntity<>(solicitudGilService.crearSolicitud(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener solicitud por ID", description = "Retorna los detalles de la solicitud y su lista de ítems.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SolicitudGilResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(solicitudGilService.obtenerId(id));
    }

    @Operation(summary = "Listar solicitudes", description = "Retorna todas las solicitudes registradas.")
    @GetMapping
    public ResponseEntity<List<SolicitudGilResponseDTO>> listarSolicitudes() {
        return ResponseEntity.ok(solicitudGilService.obtenerSolicitudes());
    }

    @Operation(summary = "Actualizar cabecera de solicitud", description = "Modifica datos básicos. Solo permitido en estado PENDIENTE.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Actualización exitosa"),
            @ApiResponse(responseCode = "400", description = "La solicitud no está en estado PENDIENTE")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SolicitudGilResponseDTO> actualizarSolicitud(
            @PathVariable Long id,
            @RequestBody SolicitudGilRequestDTO dto) {
        return ResponseEntity.ok(solicitudGilService.actualizarSolicitud(id, dto));
    }

    @Operation(summary = "Eliminar solicitud", description = "Borra físicamente la solicitud. Solo permitido en estado PENDIENTE.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSolicitud(@PathVariable Long id) {
        solicitudGilService.borrarSolicitud(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cambiar estado", description = "Flujo de aprobación: PENDIENTE -> PROCESADA -> APROBADA/RECHAZADA.")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<SolicitudGilResponseDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestBody EstadoSolicitudUpdateDTO estadoDto) {
        return ResponseEntity.ok(solicitudGilService.actualizarEstado(id, estadoDto.getNuevoEstado()));
    }
}