package com.app.Inventario.controller;

import com.app.Inventario.model.dto.response.SolicitudItemResponseDTO;
import com.app.Inventario.mapper.SolicitudItemMapper;
import com.app.Inventario.service.SolicitudItemsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes-gil/items")
@RequiredArgsConstructor
@Tag(name = "Items de Solicitud GIL", description = "Endpoints para la consulta y eliminación de bienes asociados a una solicitud.")
public class SolicitudItemsController {

    private final SolicitudItemsService solicitudItemsService;
    private final SolicitudItemMapper solicitudItemMapper;

    @Operation(
            summary = "Listar ítems de una solicitud",
            description = "Obtiene todos los bienes y sus cantidades asociados a una solicitud GIL específica."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de ítems recuperada con éxito"),
            @ApiResponse(responseCode = "404", description = "La solicitud no existe")
    })
    @GetMapping("/solicitud/{solicitudId}")
    public ResponseEntity<List<SolicitudItemResponseDTO>> listarItemsPorSolicitud(
            @Parameter(description = "ID de la solicitud GIL", required = true)
            @PathVariable Long solicitudId) {
        return ResponseEntity.ok(
                solicitudItemMapper.toResponseDtos(solicitudItemsService.listarItems(solicitudId))
        );
    }

    @Operation(
            summary = "Eliminar un ítem de la solicitud",
            description = "Elimina un ítem específico. Solo es posible si la solicitud está en estado PENDIENTE."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ítem eliminado exitosamente"),
            @ApiResponse(responseCode = "400", description = "No se puede eliminar porque la solicitud ya está aprobada o facturada"),
            @ApiResponse(responseCode = "404", description = "Ítem no encontrado")
    })
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> eliminarItem(
            @Parameter(description = "ID del ítem a eliminar", required = true)
            @PathVariable Long itemId) {
        solicitudItemsService.eliminarItem(itemId);
        return ResponseEntity.noContent().build();
    }
}