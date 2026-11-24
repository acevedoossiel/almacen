package com.ossiel.almacen.mappers;

import com.ossiel.almacen.dto.productos.ProductoRequest;
import com.ossiel.almacen.dto.productos.ProductoResponse;
import com.ossiel.almacen.entities.Producto;
import com.ossiel.almacen.enums.Categoria;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public Producto requestAEntidad(ProductoRequest request, Categoria categoria){
        return request == null
                ? null
                : Producto.crear(request.nombre(),categoria,request.precio(),request.cantidad());
    }

    public ProductoResponse entidadAResponse(Producto producto){
        return producto == null
                ? null
                : new ProductoResponse(producto.getId(),
                producto.getNombre(),
                producto.getCategoria().getDescripcion(),
                producto.getPrecio(),
                producto.getCantidad()
        );
    }
}
