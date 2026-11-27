package com.ossiel.almacen.services.ventas;

import com.ossiel.almacen.dto.ventas.DetalleVentaResponse;

import java.util.List;

public interface DetalleVentaService {
    List<DetalleVentaResponse> listar();
}
