package com.app.Inventario.controller;


import com.app.Inventario.model.dto.BienRequestDTO;
import com.app.Inventario.model.dto.BienResponseDTO;
import com.app.Inventario.service.BienService;
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
@RequestMapping("/api/bienes")
@RequiredArgsConstructor
@Tag(
        name = "Gestion de Bienes",
        description = "Operaciones CRUD para administrar bienes del inventario"
)

public class BienController {

    private final BienService bienService;

    @Operation(
            summary = "Listar todos los Bienes",
            description = "Retorna una lista completa de los bienes registrados en el sistema"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista obtenida exitosamente"
    )
    @GetMapping
    public ResponseEntity<List<BienResponseDTO>> listaBienes(){
        List<BienResponseDTO> bienes = bienService.listarTodos();
        return ResponseEntity.ok(bienes);
    }

    @Operation(
            summary = "obtener un bien por ID",
            description = "retorna la informacion de un bien especifico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bien encontrado"),
            @ApiResponse(responseCode = "404", description = "El bien no existe")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BienResponseDTO> obtenerBien(@PathVariable Long id){
        BienResponseDTO bien = bienService.obtenerPorId(id);
        return ResponseEntity.ok(bien);
    }

    @Operation(
            summary = "Crear un nuevo bien",
            description = "Crea y almacena un nuevo bien en el inventario."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Bien creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<BienResponseDTO> crearBien (@Valid @RequestBody BienRequestDTO request){
            BienResponseDTO nuevoBien = bienService.crearBien(request);
            return  ResponseEntity.status(HttpStatus.CREATED).body(nuevoBien);
    }

    @Operation(
            summary = "Actualizar un bien existente",
            description = "Modifica los datos de un bien identificado por su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bien actualizado"),
            @ApiResponse(responseCode = "404", description = "El bien no existe"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })

    @PutMapping("/{id}")
    public ResponseEntity<BienResponseDTO> actualizarBien(@PathVariable Long id, @Valid @RequestBody BienRequestDTO request){
        BienResponseDTO bienActualizado = bienService.actualizarBien(id,request);
        return ResponseEntity.ok(bienActualizado);
    }

    @Operation(
            summary = "Eliminar un bien",
            description = "Elimina un bien del inventario según su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Bien eliminado"),
            @ApiResponse(responseCode = "404", description = "El bien no existe")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarBien(@PathVariable Long id){
        bienService.eliminarBien(id);
        return ResponseEntity.noContent().build();
    }


}
