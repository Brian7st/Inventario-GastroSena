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
@Tag(name = "Items de Solicitud GIL", description = "Operaciones sobre los bienes específicos vinculados a una solicitud.")
public class SolicitudItemsController {

    private final SolicitudItemsService solicitudItemsService;
    private final SolicitudItemMapper solicitudItemMapper;

    @Operation(
            summary = "Listar ítems de una solicitud",
            description = "Recupera la lista de bienes asociados a un GIL. Utiliza carga optimizada para evitar errores de sesión."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    @GetMapping("/solicitud/{solicitudId}")
    public ResponseEntity<List<SolicitudItemResponseDTO>> listarItemsPorSolicitud(
            @Parameter(description = "ID de la solicitud GIL", required = true)
            @PathVariable Long solicitudId) {

        var itemsEntidad = solicitudItemsService.listarItems(solicitudId);

        return ResponseEntity.ok(solicitudItemMapper.toResponseDtos(itemsEntidad));
    }

    @Operation(
            summary = "Eliminar un ítem",
            description = "Borra un bien de la solicitud. Solo se permite si el GIL está en estado PENDIENTE."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ítem eliminado correctamente"),
            @ApiResponse(responseCode = "403", description = "La solicitud ya ha sido procesada y no permite cambios"),
            @ApiResponse(responseCode = "404", description = "Ítem no encontrado")
    })
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> eliminarItem(
            @Parameter(description = "ID único del ítem (detalle)", required = true)
            @PathVariable Long itemId) {

        solicitudItemsService.eliminarItem(itemId);
        return ResponseEntity.noContent().build();
    }
}