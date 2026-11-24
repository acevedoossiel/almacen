package com.ossiel.almacen.controllers;

import com.ossiel.almacen.dto.productos.ProductoRequest;
import com.ossiel.almacen.dto.productos.ProductoResponse;
import com.ossiel.almacen.services.productos.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Tag(name = "API Productos", description = "Gestió")
public class ProductoController {
    private final ProductoService productoService;

    @GetMapping
    @Operation(summary = "Listar productos", description = "Todos los diltros son opcionales")
    @ApiResponse(responseCode = "200", description = "Listado obtenido")
    public ResponseEntity<List<ProductoResponse>> listar(
            @Parameter(description = "busqueda por nombre", example = "Laptop")
            @RequestParam(required = false) String nombre,

            @Parameter(description = "busqueda por categoria", example = "Electrónico")
            @RequestParam(required = false) String categoria,

            @Parameter(description = "busqueda por minimo", example = "1000")
            @RequestParam(required = false) BigDecimal precioMIn,

            @Parameter(description = "busqueda por máximo", example = "20000")
            @RequestParam(required = false) BigDecimal precioMax

    ){
        return ResponseEntity.ok(productoService.listar(nombre,categoria,precioMIn,precioMax));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID")
    @ApiResponse(responseCode = "200", description = "Producto encontrado")
    @ApiResponse(responseCode = "404", description = "El producto no existe")
    public ResponseEntity<ProductoResponse> obtenerPorId(
            @Parameter(description = "ID del producto", example = "1")
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ){
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo producto")
    @ApiResponse(responseCode = "201", description = "Producto creado")
    public ResponseEntity<ProductoResponse> registrar(
            @Valid @RequestBody ProductoRequest request
            ){
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.registrar(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un producto existente")
    @ApiResponse(responseCode = "200", description = "Producto actualizado")
    @ApiResponse(responseCode = "404", description = "El producto no existe")
    public ResponseEntity<ProductoResponse> actualizar(
            @Parameter(description = "ID del producto", example = "1")
            @PathVariable @Positive(message = "El ID debe de ser positivo") Long id,
            @Valid @RequestBody ProductoRequest request
    ){
        return ResponseEntity.ok(productoService.actualizar(request,id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un producto")
    @ApiResponse(responseCode = "204", description = "Producto eliminado")
    @ApiResponse(responseCode = "404", description = "El producto no existe")
    public ResponseEntity<ProductoResponse> eliminar(
            @Parameter(description = "ID del producto", example = "1")
            @PathVariable @Positive(message = "El ID debe de ser positivo") Long id
    ){
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
