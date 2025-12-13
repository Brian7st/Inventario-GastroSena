package com.app.Inventario.controller;

import com.app.Inventario.model.dto.PreFacturaRequestDTO;
import com.app.Inventario.model.dto.PreFacturaResponseDTO;
import com.app.Inventario.service.PreFacturaService;
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
@RequestMapping("/api/prefacturas")
@RequiredArgsConstructor
@Tag(
        name = "Gestión de PreFacturas",
        description = "Operaciones CRUD para administrar las solicitudes de egreso/prefacturas"
)
public class PreFacturaController {

    private final PreFacturaService preFacturaService;

    @Operation(
            summary = "Crear una nueva PreFactura",
            description = "Crea y almacena una nueva prefactura con sus detalles de bienes."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "PreFactura creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o falta de stock"),
            @ApiResponse(responseCode = "404", description = "Programa de formación no encontrado")
    })
    @PostMapping
    public ResponseEntity<PreFacturaResponseDTO> crearPreFactura (@Valid @RequestBody PreFacturaRequestDTO request){
        PreFacturaResponseDTO nuevaPreFactura = preFacturaService.crearPreFactura(request);
        return  ResponseEntity.status(HttpStatus.CREATED).body(nuevaPreFactura);
    }

    @Operation(
            summary = "Listar todas las PreFacturas",
            description = "Retorna una lista completa de las prefacturas registradas en el sistema"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista obtenida exitosamente"
    )
    @GetMapping
    public ResponseEntity<List<PreFacturaResponseDTO>> listaPreFacturas(){
        List<PreFacturaResponseDTO> preFacturas = preFacturaService.listarTodos();
        return ResponseEntity.ok(preFacturas);
    }

    @Operation(
            summary = "Obtener una PreFactura por ID",
            description = "Retorna la información de una prefactura específica, incluyendo sus detalles."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "PreFactura encontrada"),
            @ApiResponse(responseCode = "404", description = "La prefactura no existe")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PreFacturaResponseDTO> obtenerPreFactura(@PathVariable Long id){
        PreFacturaResponseDTO preFactura = preFacturaService.obtenerPorId(id);
        return ResponseEntity.ok(preFactura);
    }

    @Operation(
            summary = "Actualizar una PreFactura existente",
            description = "Modifica los datos de la cabecera de una prefactura identificada por su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "PreFactura actualizada"),
            @ApiResponse(responseCode = "404", description = "La prefactura no existe"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o número duplicado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PreFacturaResponseDTO> actualizarPreFactura(@PathVariable Long id, @Valid @RequestBody PreFacturaRequestDTO request){
        PreFacturaResponseDTO preFacturaActualizada = preFacturaService.actualizarPreFactura(id,request);
        return ResponseEntity.ok(preFacturaActualizada);
    }

    @Operation(
            summary = "Anular una PreFactura",
            description = "Cambia el estado de una prefactura a ANULADA. Esto revierte el stock."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "PreFactura anulada exitosamente"),
            @ApiResponse(responseCode = "404", description = "La prefactura no existe")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> anularPreFactura(@PathVariable Long id){
        preFacturaService.anularPreFactura(id); // Usamos anular en lugar de eliminar
        return ResponseEntity.noContent().build();
    }
}