package com.app.Inventario.controller;

import com.app.Inventario.model.dto.ImpuestoDTO;
import com.app.Inventario.service.ImpuestoService;
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
@RequestMapping("/api/impuestos")
@RequiredArgsConstructor
@Tag(
        name = "Gestion de Impuestos",
        description = "Operaciones CRUD para administrar las tasas impositivas (IVA, Consumo, etc.)"
)
public class ImpuestoController {

    private final ImpuestoService impuestoService;

    @Operation(
            summary = "Listar todos los Impuestos",
            description = "Retorna una lista completa de los impuestos configurados en el sistema"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista obtenida exitosamente"
    )
    @GetMapping
    public ResponseEntity<List<ImpuestoDTO>> listarImpuestos() {
        List<ImpuestoDTO> impuestos = impuestoService.listarTodos();
        return ResponseEntity.ok(impuestos);
    }

    @Operation(
            summary = "Obtener un impuesto por ID",
            description = "Retorna la información de un impuesto específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Impuesto encontrado"),
            @ApiResponse(responseCode = "404", description = "El impuesto no existe")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ImpuestoDTO> obtenerImpuesto(@PathVariable Integer id) {
        ImpuestoDTO impuesto = impuestoService.obtenerPorId(id);
        return ResponseEntity.ok(impuesto);
    }

    @Operation(
            summary = "Crear un nuevo impuesto",
            description = "Crea y almacena una nueva tasa impositiva."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Impuesto creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o nombre duplicado")
    })
    @PostMapping
    public ResponseEntity<ImpuestoDTO> crearImpuesto(@Valid @RequestBody ImpuestoDTO request) {
        ImpuestoDTO nuevoImpuesto = impuestoService.crearImpuesto(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoImpuesto);
    }

    @Operation(
            summary = "Actualizar un impuesto existente",
            description = "Modifica los datos de un impuesto identificado por su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Impuesto actualizado"),
            @ApiResponse(responseCode = "404", description = "El impuesto no existe"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ImpuestoDTO> actualizarImpuesto(@PathVariable Integer id, @Valid @RequestBody ImpuestoDTO request) {
        ImpuestoDTO impuestoActualizado = impuestoService.actualizarImpuesto(id, request);
        return ResponseEntity.ok(impuestoActualizado);
    }

    @Operation(
            summary = "Eliminar un impuesto",
            description = "Realiza un borrado lógico (desactivación) del impuesto."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Impuesto eliminado"),
            @ApiResponse(responseCode = "404", description = "El impuesto no existe")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarImpuesto(@PathVariable Integer id) {
        impuestoService.eliminarImpuesto(id);
        return ResponseEntity.noContent().build();
    }
}