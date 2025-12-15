package com.app.Inventario.controller;

import com.app.Inventario.model.dto.FacturaGlobalRequestDTO;
import com.app.Inventario.model.dto.FacturaGlobalResponseDTO;
import com.app.Inventario.service.FacturaGlobalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facturas-globales")
@RequiredArgsConstructor
@Tag(
        name = "Gestión de Factura Global",
        description = "Operaciones CRUD para la gestión de la factura global"
)
public class FacturaGlobalController {

    private final FacturaGlobalService facturaGlobalService;

    @Operation(
            summary = "Generar Factura Global por Consolidación",
            description = "Procesa una lista de IDs de Solicitudes GIL-F-014 (estado VALIDADA) y las consolida en una nueva Factura Global. Genera el número consecutivo (RF-5.4.3) y calcula los totales (RF-5.4.4)."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Factura generada y consolidada exitosamente.",
                    content = @Content(schema = @Schema(implementation = FacturaGlobalResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos (ej. lista vacía, solicitud no validada, o ya consolidada - RF-5.4.5)"),
            @ApiResponse(responseCode = "500", description = "Error interno de servidor o de lógica de negocio (ej. falla en la numeración)")
    })
    @PostMapping
    public ResponseEntity<FacturaGlobalResponseDTO> generarFactura(
            @Valid @RequestBody FacturaGlobalRequestDTO request) {

        FacturaGlobalResponseDTO nuevaFactura = facturaGlobalService.generarFactura(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaFactura);
    }

    @Operation(
            summary = "Consultar Historial de Facturas Globales",
            description = "RF-5.4.7: Retorna un listado de todas las facturas consolidadas generadas, ideal para la vista de historial de la contadora."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista obtenida exitosamente"
    )
    @GetMapping
    public ResponseEntity<List<FacturaGlobalResponseDTO>> listarFacturasGlobales(){
        List<FacturaGlobalResponseDTO> facturas = facturaGlobalService.listarTodas();
        return ResponseEntity.ok(facturas);
    }

    @Operation(
            summary = "Exportar Factura a PDF o Excel",
            description = "RF-5.4.6: Permite descargar el documento de la factura consolidada en el formato deseado (PDF o EXCEL)."
    )
    @GetMapping("/{id}/exportar")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Archivo descargado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Factura no encontrada"),
            @ApiResponse(responseCode = "501", description = "Funcionalidad no implementada")
    })
    public ResponseEntity<byte[]> exportarFactura(
            @PathVariable Long id,
            @RequestParam(name = "formato", defaultValue = "PDF") String formato) {

        try {
            byte[] archivo = facturaGlobalService.exportar(id, formato);

            MediaType contentType = formato.equalsIgnoreCase("PDF")
                    ? MediaType.APPLICATION_PDF
                    : MediaType.APPLICATION_OCTET_STREAM;

            return ResponseEntity.ok()
                    .contentType(contentType)
                    .header("Content-Disposition", "attachment; filename=\"factura_global_" + id + "." + formato.toLowerCase() + "\"")
                    .body(archivo);

        } catch (UnsupportedOperationException e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        } 
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}