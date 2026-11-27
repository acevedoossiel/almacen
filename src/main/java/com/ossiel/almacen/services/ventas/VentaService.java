package com.ossiel.almacen.services.ventas;

import com.ossiel.almacen.dto.ventas.DetalleVentaRequest;
import com.ossiel.almacen.dto.ventas.ReporteVentasSucursalResponse;
import com.ossiel.almacen.dto.ventas.VentaRequest;
import com.ossiel.almacen.dto.ventas.VentaResponse;

import java.util.List;

public interface VentaService {
    List<VentaResponse> listar(Long id, List<DetalleVentaRequest> productos);

    VentaResponse obtenerPorIdActiva(Long id);

    VentaResponse registrar (VentaRequest request);

    void cancelar(Long id);

    List<VentaResponse> listarCanceladas();

    List<ReporteVentasSucursalResponse> obtenerReportePorSucursal();
}
