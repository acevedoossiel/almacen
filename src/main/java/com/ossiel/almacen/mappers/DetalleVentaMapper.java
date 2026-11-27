package com.ossiel.almacen.mappers;

import com.ossiel.almacen.dto.productos.ProductoResponse;
import com.ossiel.almacen.dto.ventas.DetalleVentaRequest;
import com.ossiel.almacen.dto.ventas.DetalleVentaResponse;
import com.ossiel.almacen.entities.DetalleVenta;
import com.ossiel.almacen.entities.Producto;
import com.ossiel.almacen.entities.Venta;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DetalleVentaMapper {

    public DetalleVentaMapper() {}

    public DetalleVenta requestAEntidad(DetalleVentaRequest request, Producto producto) {
        return request == null
                ? null
                : DetalleVenta.crear(producto,
                request.cantidadProducto());
    }

    public DetalleVentaResponse entidadAResponse(DetalleVenta detalle) {
        return detalle == null
                ? null
                : new DetalleVentaResponse(
                detalle.getProducto().getId(),
                detalle.getProducto().getNombre(),
                detalle.getCantidadProducto(),
                detalle.getPrecioProducto(),
                detalle.calcularSubtotal()
        );
    }
}

