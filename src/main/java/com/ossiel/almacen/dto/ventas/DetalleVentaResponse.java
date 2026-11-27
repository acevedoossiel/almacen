package com.ossiel.almacen.dto.ventas;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Detalle de un producto de una venta")
public record DetalleVentaResponse(
        @Schema(description = "ID del producto", example = "1")
        Long idProducto,

        @Schema(description = "Nombre del producto", example = "Producto 1")
        String nombreProducto,

        @Schema(description = "Cantidad vendida", example = "5")
        Integer cantidadProducto,

        @Schema(description = "Precio por pieza del producto" , example = "50.50")
        BigDecimal precioProducto,

        @Schema(description = "Subtotal del producto", example = "7500.00")
        BigDecimal subtotal
) {
}
