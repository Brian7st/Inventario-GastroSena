package com.app.Inventario.controller;

import com.app.Inventario.model.dto.request.ProgramaRequestDTO;
import com.app.Inventario.model.dto.response.ProgramaResponseDTO;
import com.app.Inventario.service.ProgramaService;
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
@RequestMapping("/api/programas")
@RequiredArgsConstructor
@Tag(
        name = "Gestión de Programas de Formación",
        description = "Operaciones CRUD para administrar los programas de formación/fichas"
)
public class ProgramaFormacionController {

    private final ProgramaService programaService;

    @Operation(
            summary = "Listar todos los Programas",
            description = "Retorna una lista completa de los programas de formación registrados."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista obtenida exitosamente"
    )
    @GetMapping
    public ResponseEntity<List<ProgramaResponseDTO>> listarProgramas(){
        List<ProgramaResponseDTO> programas = programaService.listarTodos();
        return ResponseEntity.ok(programas);
    }

    @Operation(
            summary = "Obtener un Programa por ID",
            description = "Retorna la información de un programa específico."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Programa encontrado"),
            @ApiResponse(responseCode = "404", description = "El programa no existe")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProgramaResponseDTO> obtenerPrograma(@PathVariable Long id){
        ProgramaResponseDTO programa = programaService.obtenerPorId(id);
        return ResponseEntity.ok(programa);
    }

    @Operation(
            summary = "Crear un nuevo Programa",
            description = "Crea y almacena un nuevo programa de formación."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Programa creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<ProgramaResponseDTO> crearPrograma (@Valid @RequestBody ProgramaRequestDTO request){
        ProgramaResponseDTO nuevoPrograma = programaService.crearPrograma(request);
        return  ResponseEntity.status(HttpStatus.CREATED).body(nuevoPrograma);
    }

    @Operation(
            summary = "Actualizar un Programa existente",
            description = "Modifica los datos de un programa identificado por su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Programa actualizado"),
            @ApiResponse(responseCode = "404", description = "El programa no existe"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProgramaResponseDTO> actualizarPrograma(@PathVariable Long id, @Valid @RequestBody ProgramaRequestDTO request){
        ProgramaResponseDTO programaActualizado = programaService.actualizarPrograma(id,request);
        return ResponseEntity.ok(programaActualizado);
    }

    @Operation(
            summary = "Eliminar un Programa (Lógico)",
            description = "Cambia el estado del programa a INACTIVO según su ID (Eliminación Lógica)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Programa eliminado"),
            @ApiResponse(responseCode = "404", description = "El programa no existe")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPrograma(@PathVariable Long id){
        programaService.eliminarPrograma(id); // Suponiendo eliminación lógica (cambio de estado)
        return ResponseEntity.noContent().build();
    }
}