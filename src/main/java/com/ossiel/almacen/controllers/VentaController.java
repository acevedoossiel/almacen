package com.ossiel.almacen.controllers;

import com.ossiel.almacen.docs.ProblemaDoc;
import com.ossiel.almacen.dto.ventas.ReporteVentasSucursalResponse;
import com.ossiel.almacen.dto.ventas.VentaRequest;
import com.ossiel.almacen.dto.ventas.VentaResponse;
import com.ossiel.almacen.services.ventas.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
@Tag(name = "API Ventas", description = "Gestión")
public class  VentaController {

    private final VentaService ventaService;

    @GetMapping
    @Operation(summary = "Listar ventas registradas", description = "Ventas con estatus REGISTRADA")
    @ApiResponse(responseCode = "200", description = "Listado de ventas activas")
    public ResponseEntity<List<VentaResponse>> listar(
            @Parameter(description = "Búsqueda por ID de venta", example = "1")
            @RequestParam(required = false) Long id
    ) {
        return ResponseEntity.ok(ventaService.listar(id, null));
    }

    @GetMapping("/canceladas")
    @Operation(summary = "Listar ventas canceladas", description = "Retorna el histórico de ventas en estado CANCELADA")
    @ApiResponse(responseCode = "200", description = "Listado de ventas canceladas")
    public ResponseEntity<List<VentaResponse>> listarCanceladas() {
        return ResponseEntity.ok(ventaService.listarCanceladas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener venta activa por ID")
    @ApiResponse(responseCode = "200", description = "Venta activa encontrada")
    @ApiResponse(responseCode = "404", description = "La venta no existe o se encuentra cancelada",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<VentaResponse> obtenerPorIdActiva(
            @Parameter(description = "ID de la venta", example = "1")
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ) {
        return ResponseEntity.ok(ventaService.obtenerPorIdActiva(id));
    }

    @PostMapping
    @Operation(summary = "Registrar una nueva venta")
    @ApiResponse(responseCode = "201", description = "Venta registrada")
    @ApiResponse(responseCode = "400", description = "Datos inválidos o stock insuficiente",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    @ApiResponse(responseCode = "404", description = "Sucursal o producto no encontrado",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<VentaResponse> registrar(
            @Valid @RequestBody VentaRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaService.registrar(request));
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar una venta", description = "Cambia el estado a CANCELADA y devuelve el stock a los productos")
    @ApiResponse(responseCode = "204", description = "Venta cancelada correctamente")
    @ApiResponse(responseCode = "404", description = "La venta no existe",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    @ApiResponse(responseCode = "409", description = "La venta ya se encuentra cancelada",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<Void> cancelar(
            @Parameter(description = "ID de la venta", example = "1")
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ) {
        ventaService.cancelar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reporte-sucursales")
    @Operation(
            summary = "Reporte de ventas por sucursal",
            description = "Retorna el total y la cantidad de productos vendidos por cada sucursal"
    )
    @ApiResponse(responseCode = "200", description = "Reporte generado correctamente")
    public ResponseEntity<List<ReporteVentasSucursalResponse>> obtenerReportePorSucursal() {
        return ResponseEntity.ok(ventaService.obtenerReportePorSucursal());
    }
}