package com.app.Inventario.controller;

import com.app.Inventario.model.dto.EstadoSolicitudUpdateDTO;
import com.app.Inventario.model.dto.request.SolicitudGilRequestDTO;
import com.app.Inventario.model.dto.response.SolicitudGilResponseDTO;
import com.app.Inventario.service.SolicitudGilService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid; // Asegúrate de tener esta importación
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
            @ApiResponse(responseCode = "201", description = "Solicitud creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o Pre-Factura no apta")
    })
    @PostMapping
    public ResponseEntity<SolicitudGilResponseDTO> crearSolicitud(@Valid @RequestBody SolicitudGilRequestDTO dto) {
        return new ResponseEntity<>(solicitudGilService.crearSolicitud(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener solicitud por ID", description = "Retorna los detalles de la solicitud, incluyendo ítems congelados y cuentadantes.")
    @GetMapping("/{id}")
    public ResponseEntity<SolicitudGilResponseDTO> obtenerPorId(@PathVariable Long id) {
        // El servicio ya usa findFullById para evitar LazyInitializationException
        return ResponseEntity.ok(solicitudGilService.obtenerId(id));
    }

    @Operation(summary = "Listar todas las solicitudes", description = "Retorna el histórico de solicitudes GIL.")
    @GetMapping
    public ResponseEntity<List<SolicitudGilResponseDTO>> listarSolicitudes() {
        return ResponseEntity.ok(solicitudGilService.obtenerSolicitudes());
    }

    @Operation(summary = "Actualizar datos básicos", description = "Permite modificar datos de cabecera si la solicitud sigue PENDIENTE.")
    @PutMapping("/{id}")
    public ResponseEntity<SolicitudGilResponseDTO> actualizarSolicitud(
            @PathVariable Long id,
            @Valid @RequestBody SolicitudGilRequestDTO dto) {
        return ResponseEntity.ok(solicitudGilService.actualizarSolicitud(id, dto));
    }

    @Operation(summary = "Eliminar solicitud", description = "Elimina físicamente el registro. Solo permitido en estado PENDIENTE.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSolicitud(@PathVariable Long id) {
        solicitudGilService.borrarSolicitud(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cambiar estado de la solicitud", description = "Actualiza el ciclo de vida (PENDIENTE, PROCESADA, APROBADA, RECHAZADA).")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<SolicitudGilResponseDTO> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody EstadoSolicitudUpdateDTO estadoDto) {
        return ResponseEntity.ok(solicitudGilService.actualizarEstado(id, estadoDto.getNuevoEstado()));
    }
}