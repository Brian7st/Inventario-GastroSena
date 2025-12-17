package com.app.Inventario.controller;

import com.app.Inventario.model.entity.SolicitudItems;
import com.app.Inventario.service.SolicitudItemsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Tag(name = "Items de Solicitud GIL", description = "Operaciones para gestionar los bienes (items) dentro de una solicitud GIL específica.")
public class SolicitudItemsController {

    private final SolicitudItemsService solicitudItemsService;

    @Operation(summary = "Agregar un item a una solicitud", description = "Asocia un nuevo item (bien y cantidad) a una solicitud existente identificada por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item agregado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SolicitudItems.class))),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada", content = @Content),
            @ApiResponse(responseCode = "400", description = "Datos del item inválidos o solicitud en estado no editable", content = @Content)
    })
    @PostMapping("/{solicitudId}")
    public ResponseEntity<SolicitudItems> agregarItem(
            @Parameter(description = "ID de la solicitud GIL a la cual se agregará el item", required = true, example = "1")
            @PathVariable Long solicitudId,
            @RequestBody SolicitudItems item) {
        return ResponseEntity.ok(
                solicitudItemsService.agregarItems(solicitudId, item)
        );
    }

    @Operation(summary = "Actualizar un item existente", description = "Modifica la cantidad o el bien asociado de un item específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item actualizado correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SolicitudItems.class))),
            @ApiResponse(responseCode = "404", description = "Item no encontrado", content = @Content)
    })
    @PutMapping("/{itemId}")
    public ResponseEntity<SolicitudItems> actualizarItem(
            @Parameter(description = "ID del item que se desea actualizar", required = true, example = "5")
            @PathVariable Long itemId,
            @RequestBody SolicitudItems item) {
        return ResponseEntity.ok(
                solicitudItemsService.actualizarItems(itemId, item)
        );
    }

    @Operation(summary = "Eliminar un item", description = "Elimina un item de una solicitud. Solo permitido si la solicitud está en estado PENDIENTE.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item eliminado correctamente (sin contenido)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Item no encontrado", content = @Content)
    })
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> eliminarItem(
            @Parameter(description = "ID del item a eliminar", required = true, example = "5")
            @PathVariable Long itemId) {
        solicitudItemsService.eliminarItem(itemId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar items de una solicitud", description = "Obtiene la lista de todos los items asociados a una solicitud específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista recuperada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SolicitudItems.class))),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada", content = @Content)
    })
    @GetMapping("/solicitud/{solicitudId}")
    public ResponseEntity<List<SolicitudItems>> listarItemsPorSolicitud(
            @Parameter(description = "ID de la solicitud para consultar sus items", required = true, example = "1")
            @PathVariable Long solicitudId) {
        return ResponseEntity.ok(
                solicitudItemsService.listarItems(solicitudId)
        );
    }
}