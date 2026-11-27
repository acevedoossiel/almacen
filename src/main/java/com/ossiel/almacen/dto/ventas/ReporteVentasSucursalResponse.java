package com.ossiel.almacen.dto.ventas;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Reporte de ventas activas por sucursal")
public record ReporteVentasSucursalResponse(
        @Schema(description = "ID de la sucursal", example = "1")
        Long idSucursal,

        @Schema(description = "Nombre de la sucursal", example = "Sucursal 1")
        String nombreSucursal,

        @Schema(description = "Total facturado de ventas activas", example = "1500")
        BigDecimal totalFacturado,

        @Schema(description = "Cantidad total de productos vendidos", example = "2")
        Long cantidadProductosVendidos
) {
}
