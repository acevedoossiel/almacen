package com.ossiel.almacen.services.productos;

import com.ossiel.almacen.dto.productos.ProductoRequest;
import com.ossiel.almacen.dto.productos.ProductoResponse;
import com.ossiel.almacen.entities.Producto;

import java.math.BigDecimal;
import java.util.List;

public interface ProductoService {

    List<ProductoResponse> listar(
            String nombre, String categoria,
            BigDecimal precioMin,BigDecimal precioMax
    );

    ProductoResponse obtenerPorId(Long id);

    ProductoResponse registrar(ProductoRequest request);

    ProductoResponse actualizar(ProductoRequest request, Long id);

    void eliminar(Long id);
}
