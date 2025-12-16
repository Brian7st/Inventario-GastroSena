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
@Tag(name = "Solicitudes GIL", description = "Endpoints para la gestión, creación y flujo de estados de las solicitudes GIL (Gestión de Inventarios y Logística).")
public class SolicitudGilController {

    private final SolicitudGilService solicitudGilService;

    @Operation(summary = "Crear nueva solicitud", description = "Registra una nueva solicitud GIL en el sistema con estado inicial PENDIENTE.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Solicitud creada exitosamente", // Cambiado a 201 Created
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SolicitudGilResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<SolicitudGilResponseDTO> crearSolicitud(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO con los datos de la nueva solicitud", required = true, content = @Content(schema = @Schema(implementation = SolicitudGilRequestDTO.class)))
            @RequestBody SolicitudGilRequestDTO solicitudGilRequestDTO) {
        return new ResponseEntity<>(
                solicitudGilService.crearSolicitud(solicitudGilRequestDTO),
                HttpStatus.CREATED // Retorna 201
        );
    }

    @Operation(summary = "Obtener solicitud por ID", description = "Busca y retorna los detalles de una solicitud específica mediante su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SolicitudGilResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada con el ID proporcionado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<SolicitudGilResponseDTO> obtenerPorId(
            @Parameter(description = "ID de la solicitud a buscar", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(
                solicitudGilService.obtenerId(id)
        );
    }

    @Operation(summary = "Listar todas las solicitudes", description = "Retorna un listado completo de todas las solicitudes registradas en el sistema.")
    @ApiResponse(responseCode = "200", description = "Operación exitosa",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SolicitudGilResponseDTO.class)))
    @GetMapping
    public ResponseEntity<List<SolicitudGilResponseDTO>> listarSolicitudes() {
        return ResponseEntity.ok(
                solicitudGilService.obtenerSolicitudes()
        );
    }

    @Operation(summary = "Actualizar solicitud", description = "Modifica los datos de una solicitud existente. Solo permitido si la solicitud se encuentra en estado PENDIENTE.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud actualizada correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SolicitudGilResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada", content = @Content),
            @ApiResponse(responseCode = "400", description = "No se puede editar una solicitud que no esté en estado PENDIENTE", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<SolicitudGilResponseDTO> actualizarSolicitud(
            @Parameter(description = "ID de la solicitud a actualizar", required = true, example = "1")
            @PathVariable Long id,
            // Corregido: Se usa SolicitudGilRequestDTO en lugar de SolicitudGil
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO con los datos actualizados de la solicitud", required = true, content = @Content(schema = @Schema(implementation = SolicitudGilRequestDTO.class)))
            @RequestBody SolicitudGilRequestDTO solicitudGilRequestDTO) {
        return ResponseEntity.ok(
                solicitudGilService.actualizarSolicitud(id, solicitudGilRequestDTO)
        );
    }

    @Operation(summary = "Eliminar solicitud", description = "Elimina una solicitud del sistema. Solo permitido si la solicitud está en estado PENDIENTE.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Solicitud eliminada correctamente (No Content)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada", content = @Content),
            @ApiResponse(responseCode = "400", description = "No se puede eliminar una solicitud que no esté en estado PENDIENTE", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSolicitud(
            @Parameter(description = "ID de la solicitud a eliminar", required = true, example = "1")
            @PathVariable Long id) {
        // Corregido: Cambiado a 'borrarSolicitud' (minúscula) para coincidir con el Service
        solicitudGilService.borrarSolicitud(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Asociar Pre-Factura", description = "Vincula una Pre-Factura existente a una Solicitud GIL PENDIENTE, transfiere los ítems y asocia la Pre-Factura.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asociación exitosa",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SolicitudGilResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Solicitud o Pre-Factura no encontrada", content = @Content),
            @ApiResponse(responseCode = "400", description = "La solicitud ya tiene Pre-Factura o no está PENDIENTE, o la Pre-Factura no está VALIDADA", content = @Content)
    })
    @PostMapping("/{solicitudId}/prefactura/{preFacturaId}")
    public ResponseEntity<SolicitudGilResponseDTO> asociarPreFactura(
            @Parameter(description = "ID de la solicitud GIL PENDIENTE", required = true, example = "10")
            @PathVariable Long solicitudId,
            @Parameter(description = "ID de la Pre-Factura VALIDADA a asociar", required = true, example = "50")
            @PathVariable Long preFacturaId) {
        return ResponseEntity.ok(
                solicitudGilService.asociarPreFactura(solicitudId, preFacturaId)
        );
    }
    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambiar el estado de la solicitud",
            description = "Permite a un usuario con permisos APROBAR, RECHAZAR o CANCELAR una solicitud PENDIENTE.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SolicitudGilResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada", content = @Content),
            @ApiResponse(responseCode = "400", description = "El cambio de estado viola las reglas de flujo de negocio", content = @Content)
    })
    public ResponseEntity<SolicitudGilResponseDTO> actualizarEstado(
            @Parameter(description = "ID de la solicitud", required = true)
            @PathVariable Long id,
            @RequestBody EstadoSolicitudUpdateDTO estadoDto) {

        return ResponseEntity.ok(
                solicitudGilService.actualizarEstado(id, estadoDto.getNuevoEstado())
        );
    }
}