package com.app.Inventario.controller;

import com.app.Inventario.model.dto.CategoriaDTO;
import com.app.Inventario.service.CategoriaService;
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
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
@Tag(
        name = "Gestion de Categorias",
        description = "Operaciones CRUD para administrar categorías del inventario"
)
public class CategoriaController {

    private final CategoriaService categoriaService;

    @Operation(
            summary = "Listar todas las Categorías",
            description = "Retorna una lista completa de las categorías registradas en el sistema"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista obtenida exitosamente"
    )
    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> listarCategorias() {
        List<CategoriaDTO> categorias = categoriaService.listarTodas();
        return ResponseEntity.ok(categorias);
    }

    @Operation(
            summary = "Obtener una categoría por ID",
            description = "Retorna la información de una categoría específica"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría encontrada"),
            @ApiResponse(responseCode = "404", description = "La categoría no existe")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> obtenerCategoria(@PathVariable Integer id) {
        CategoriaDTO categoria = categoriaService.obtenerPorId(id);
        return ResponseEntity.ok(categoria);
    }

    @Operation(
            summary = "Crear una nueva categoría",
            description = "Crea y almacena una nueva categoría en el inventario."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o nombre duplicado")
    })
    @PostMapping
    public ResponseEntity<CategoriaDTO> crearCategoria(@Valid @RequestBody CategoriaDTO request) {
        CategoriaDTO nuevaCategoria = categoriaService.crearCategoria(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCategoria);
    }

    @Operation(
            summary = "Actualizar una categoría existente",
            description = "Modifica los datos de una categoría identificada por su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría actualizada"),
            @ApiResponse(responseCode = "404", description = "La categoría no existe"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> actualizarCategoria(@PathVariable Integer id, @Valid @RequestBody CategoriaDTO request) {
        CategoriaDTO categoriaActualizada = categoriaService.actualizarCategoria(id, request);
        return ResponseEntity.ok(categoriaActualizada);
    }

    @Operation(
            summary = "Eliminar una categoría",
            description = "Elimina (o desactiva) una categoría del inventario según su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Categoría eliminada"),
            @ApiResponse(responseCode = "404", description = "La categoría no existe")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Integer id) {
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}