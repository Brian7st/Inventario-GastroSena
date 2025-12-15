package com.app.Inventario.controller;

import com.app.Inventario.model.dto.UnidadMedidaDTO;
import com.app.Inventario.service.UnidadMedidaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/unidades-medida")
@RequiredArgsConstructor
@Tag(
        name = "Gestion de Unidades de Medida",
        description = "Operaciones CRUD para administrar unidades (Kg, Lts, Unidad, etc.)"
)
public class UnidadMedidaController {

    private final UnidadMedidaService unidadMedidaService;

    @Operation(
            summary = "Listar todas las Unidades",
            description = "Retorna una lista completa de las unidades de medida configuradas"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista obtenida exitosamente"
    )
    @GetMapping
    public ResponseEntity<List<UnidadMedidaDTO>> listarUnidades() {
        List<UnidadMedidaDTO> unidades = unidadMedidaService.listarTodas();
        return ResponseEntity.ok(unidades);
    }

    @Operation(
            summary = "Obtener una unidad por ID",
            description = "Retorna la información de una unidad específica"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Unidad encontrada"),
            @ApiResponse(responseCode = "404", description = "La unidad no existe")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UnidadMedidaDTO> obtenerUnidad(@PathVariable Integer id) {
        UnidadMedidaDTO unidad = unidadMedidaService.obtenerPorId(id);
        return ResponseEntity.ok(unidad);
    }

    @Operation(
            summary = "Crear una nueva unidad",
            description = "Crea y almacena una nueva unidad de medida."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Unidad creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o abreviatura duplicada")
    })
    @PostMapping
    public ResponseEntity<UnidadMedidaDTO> crearUnidad(@Valid @RequestBody UnidadMedidaDTO request) {
        UnidadMedidaDTO nuevaUnidad = unidadMedidaService.crearUnidadMedida(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaUnidad);
    }

    @Operation(
            summary = "Actualizar una unidad existente",
            description = "Modifica los datos de una unidad de medida identificada por su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Unidad actualizada"),
            @ApiResponse(responseCode = "404", description = "La unidad no existe"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UnidadMedidaDTO> actualizarUnidad(@PathVariable Integer id, @Valid @RequestBody UnidadMedidaDTO request) {
        UnidadMedidaDTO unidadActualizada = unidadMedidaService.actualizarUnidadMedida(id, request);
        return ResponseEntity.ok(unidadActualizada);
    }

    @Operation(
            summary = "Eliminar una unidad",
            description = "Realiza un borrado lógico (desactivación) de la unidad de medida."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Unidad eliminada"),
            @ApiResponse(responseCode = "404", description = "La unidad no existe")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUnidad(@PathVariable Integer id) {
        unidadMedidaService.eliminarUnidadMedida(id);
        return ResponseEntity.noContent().build();
    }
}