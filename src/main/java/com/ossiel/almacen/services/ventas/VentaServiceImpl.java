package com.ossiel.almacen.services.ventas;

import com.ossiel.almacen.dto.ventas.DetalleVentaRequest;
import com.ossiel.almacen.dto.ventas.ReporteVentasSucursalResponse;
import com.ossiel.almacen.dto.ventas.VentaRequest;
import com.ossiel.almacen.dto.ventas.VentaResponse;
import com.ossiel.almacen.entities.DetalleVenta;
import com.ossiel.almacen.entities.Producto;
import com.ossiel.almacen.entities.Sucursal;
import com.ossiel.almacen.entities.Venta;
import com.ossiel.almacen.enums.EstadoVenta;
import com.ossiel.almacen.exceptions.RecursoNoEncontradoException;
import com.ossiel.almacen.mappers.DetalleVentaMapper;
import com.ossiel.almacen.mappers.VentaMapper;
import com.ossiel.almacen.repositories.ProductoRepository;
import com.ossiel.almacen.repositories.SucursalRepository;
import com.ossiel.almacen.repositories.VentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final SucursalRepository sucursalRepository;
    private final ProductoRepository productoRepository;
    private final VentaMapper ventaMapper;
    private final DetalleVentaMapper detalleVentaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> listar(Long id, List<DetalleVentaRequest> productos) {
        log.info("Listando ventas registradas (activas)");

        Set<Long> idsProductosFiltro = (productos == null || productos.isEmpty())
                ? null
                : productos.stream()
                .map(DetalleVentaRequest::idProducto)
                .collect(Collectors.toSet());

        return ventaRepository.findByEstadoVentaOrderByIdDesc(EstadoVenta.REGISTRADA)
                .stream()
                .filter(v -> id == null || v.getId().equals(id))
                .filter(v -> idsProductosFiltro.isEmpty() || v.getDetalleVentas().stream()
                        .anyMatch(d -> idsProductosFiltro.contains(d.getProducto().getId())))
                .map(ventaMapper::entidadAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> listarCanceladas() {
        log.info("Listando histórico de ventas canceladas");
        return ventaRepository.findByEstadoVentaOrderByIdDesc(EstadoVenta.CANCELADA)
                .stream()
                .map(ventaMapper::entidadAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VentaResponse obtenerPorIdActiva(Long id) {
        log.info("Buscando venta activa con id: {}", id);
        Venta venta = ventaRepository.findByIdAndEstadoVenta(id, EstadoVenta.REGISTRADA)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Venta activa no encontrada con id: " + id));
        return ventaMapper.entidadAResponse(venta);
    }

    @Override
    public VentaResponse registrar(VentaRequest request) {
        log.info("Registrando nueva venta en sucursal id: {}", request.idSucursal());
        Sucursal sucursal = obtenerSucursalOException(request.idSucursal());

        Venta venta = ventaMapper.requestAEntidad(sucursal);

        for (DetalleVentaRequest detalleRequest : request.productos()) {
            Producto producto = obtenerProductoOException(detalleRequest.idProducto());

            producto.descontarCantidad(detalleRequest.cantidadProducto());

            DetalleVenta detalle = detalleVentaMapper.requestAEntidad(detalleRequest, producto);
            venta.agregarDetalle(detalle);
        }

        ventaRepository.saveAndFlush(venta);
        log.info("Venta registrada correctamente con id: {} y total: {}", venta.getId(), venta.calcularTotal());
        return ventaMapper.entidadAResponse(venta);
    }

    @Override
    public void cancelar(Long id) {
        Venta venta = obtenerVentaOException(id);
        log.info("Cancelando venta con id: {}", id);

        venta.cancelar();

        for (DetalleVenta detalle : venta.getDetalleVentas()) {
            Producto producto = detalle.getProducto();
            producto.aumentarCantidad(detalle.getCantidadProducto());
        }

        ventaRepository.saveAndFlush(venta);
        log.info("Venta con id: {} cancelada y stock devuelto correctamente", id);
    }

    private Venta obtenerVentaOException(Long id) {
        log.info("Buscando venta con id: {}", id);
        return ventaRepository.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("Venta no encontrada con id: " + id)
        );
    }

    private Sucursal obtenerSucursalOException(Long id) {
        log.info("Buscando sucursal con id: {}", id);
        return sucursalRepository.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("Sucursal no encontrada con id: " + id)
        );
    }

    private Producto obtenerProductoOException(Long id) {
        log.info("Buscando producto con id: {}", id);
        return productoRepository.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("Producto no encontrado con id: " + id)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteVentasSucursalResponse> obtenerReportePorSucursal() {
        log.info("Generando reporte de ventas por sucursal");
        return ventaRepository.obtenerReporteVentasPorSucursal();
    }
}