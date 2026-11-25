package com.ossiel.almacen.services.sucursales;

import com.ossiel.almacen.dto.productos.ProductoRequest;
import com.ossiel.almacen.dto.sucursales.SucursalRequest;
import com.ossiel.almacen.dto.sucursales.SucursalResponse;

import java.util.List;

public interface SucursalService {
    List<SucursalResponse> listar(String nombre, String direccion);

    SucursalResponse obtenerPorId(Long id);

    SucursalResponse registrar(SucursalRequest request);

    SucursalResponse actualizar(SucursalRequest request, Long id);

    void eliminar(Long id);



}
