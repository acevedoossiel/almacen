package com.ossiel.almacen.mappers;


import com.ossiel.almacen.dto.ventas.DetalleVentaResponse;
import com.ossiel.almacen.dto.ventas.VentaResponse;
import com.ossiel.almacen.entities.Sucursal;
import com.ossiel.almacen.entities.Venta;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;


@Component
@RequiredArgsConstructor
public class VentaMapper {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final SucursalMapper sucursalMapper;
    private final DetalleVentaMapper detalleVentaMapper;

    public Venta requestAEntidad(Sucursal sucursal) {
        return (sucursal == null)
                ? null
                : Venta.crear(sucursal);
    }

    public VentaResponse entidadAResponse(Venta venta) {
        if (venta == null) {
            return null;
        }

        List<DetalleVentaResponse> detalles = (venta.getDetalleVentas() == null)
                ? null
                : venta.getDetalleVentas().stream()
                        .map(detalleVentaMapper::entidadAResponse)
                        .toList();

        return new VentaResponse(
                venta.getId(),
                venta.getFecha().format(FORMATO_FECHA),
                venta.getEstadoVenta().getDescripcion(),
                sucursalMapper.entidadAResponse(venta.getSucursal()),
                detalles,
                venta.calcularTotal()
        );
    }
}