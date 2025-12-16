package com.app.Inventario.controller;

import com.app.Inventario.model.dto.request.PreFacturaRequestDTO;
import com.app.Inventario.model.dto.response.PreFacturaResponseDTO;
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
            @ApiResponse(responseCode = "400", description = "Datos inválidos (ej. ID de programa o bien no encontrado)"),
            @ApiResponse(responseCode = "409", description = "El número de prefactura ya existe")
    })
    @PostMapping
    public ResponseEntity<PreFacturaResponseDTO> crearPreFactura (@Valid @RequestBody PreFacturaRequestDTO request){
        PreFacturaResponseDTO nuevaPreFactura = preFacturaService.crearPreFactura(request);
        return  ResponseEntity.status(HttpStatus.CREATED).body(nuevaPreFactura);
    }


    @Operation(
            summary = "Listar todas las PreFacturas (con filtros opcionales)",
            description = "Retorna una lista de prefacturas, permitiendo filtrar por número, estado o programa."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista obtenida exitosamente"
    )
    @GetMapping
    public ResponseEntity<List<PreFacturaResponseDTO>> listaPreFacturas(

            @RequestParam(required = false) String numero,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Long programaId
    ){

        List<PreFacturaResponseDTO> preFacturas = preFacturaService.listarConFiltros(numero, estado, programaId);
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
            description = "Modifica los datos (cabecera y detalles) de una prefactura PENDIENTE, identificada por su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "PreFactura actualizada"),
            @ApiResponse(responseCode = "404", description = "PreFactura o programa no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o no se puede actualizar (no está PENDIENTE)"),
            @ApiResponse(responseCode = "409", description = "El número de prefactura ya está en uso")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PreFacturaResponseDTO> actualizarPreFactura(@PathVariable Long id, @Valid @RequestBody PreFacturaRequestDTO request){
        PreFacturaResponseDTO preFacturaActualizada = preFacturaService.actualizarPreFactura(id,request);
        return ResponseEntity.ok(preFacturaActualizada);
    }


    @Operation(
            summary = "Validar una PreFactura",
            description = "Cambia el estado de la prefactura a VALIDADA. Solo se permite si está PENDIENTE."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "PreFactura validada"),
            @ApiResponse(responseCode = "404", description = "PreFactura no existe"),
            @ApiResponse(responseCode = "400", description = "No se puede validar (ya anulada/validada)")
    })
    @PatchMapping("/{id}/validar")
    public ResponseEntity<PreFacturaResponseDTO> validarPreFactura(@PathVariable Long id) {
        PreFacturaResponseDTO preFacturaValidada = preFacturaService.validarPreFactura(id);
        return ResponseEntity.ok(preFacturaValidada);
    }


    @Operation(
            summary = "Anular una PreFactura",
            description = "Cambia el estado de una prefactura a ANULADA (Eliminación lógica). **Esta acción NO afecta el stock del inventario.**"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "PreFactura anulada exitosamente"),
            @ApiResponse(responseCode = "404", description = "La prefactura no existe")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> anularPreFactura(@PathVariable Long id){
        preFacturaService.anularPreFactura(id);
        return ResponseEntity.noContent().build();
    }
}