package com.ossiel.almacen.repositories;

import com.ossiel.almacen.dto.ventas.ReporteVentasSucursalResponse;
import com.ossiel.almacen.entities.Venta;
import com.ossiel.almacen.enums.EstadoVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findByEstadoVentaOrderByIdDesc(EstadoVenta estadoVenta);

    Optional<Venta> findByIdAndEstadoVenta(Long id, EstadoVenta estadoVenta);

    @Query("""
            SELECT new com.ossiel.almacen.dto.ventas.ReporteVentasSucursalResponse(
                s.id,
                s.nombre,
                COALESCE(SUM(d.precioProducto * d.cantidadProducto), 0),
                COALESCE(SUM(d.cantidadProducto), 0L)
            )
            FROM Sucursal s
            LEFT JOIN Venta v ON v.sucursal = s
                AND v.estadoVenta = com.ossiel.almacen.enums.EstadoVenta.REGISTRADA
            LEFT JOIN v.detalleVentas d
            GROUP BY s.id, s.nombre
            ORDER BY s.id ASC
            """)
    List<ReporteVentasSucursalResponse> obtenerReporteVentasPorSucursal();
}