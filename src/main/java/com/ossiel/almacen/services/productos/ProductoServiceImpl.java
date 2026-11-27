package com.ossiel.almacen.services.productos;

import com.ossiel.almacen.dto.productos.ProductoRequest;
import com.ossiel.almacen.dto.productos.ProductoResponse;
import com.ossiel.almacen.entities.Producto;
import com.ossiel.almacen.enums.Categoria;
import com.ossiel.almacen.exceptions.DatoInvalidoException;
import com.ossiel.almacen.exceptions.RecursoNoEncontradoException;
import com.ossiel.almacen.mappers.ProductoMapper;
import com.ossiel.almacen.repositories.ProductoRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductoServiceImpl implements ProductoService{

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listar(String nombre,
                                         String categoria,
                                         BigDecimal precioMin,
                                         BigDecimal precioMax) {
        log.info("Listando productos tomando en cuenta los campos como nombre: {}, categoria: {}, precioMin: {}, precioMax: {}",
                nombre, categoria, precioMin, precioMax);

        if (precioMin != null && precioMax != null && precioMin.compareTo(precioMax) > 0) {
            throw new DatoInvalidoException("El precio mínimo no puede ser mayor al precio máximo");
        }

        String nombreFiltro = (nombre != null && !nombre.isBlank()) ? nombre.trim() : null;
        Categoria categoriaFiltro = (categoria != null && !categoria.isBlank())
                ? Categoria.obtenerCategoriaPorDescripcion(categoria.trim())
                : null;

        return productoRepository.buscarConFiltros(nombreFiltro, categoriaFiltro, precioMin, precioMax)
                .stream()
                .map(productoMapper::entidadAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorId(Long id) {
        return productoMapper.entidadAResponse(obtenerProductoOException(id));
    }

    @Override
    public ProductoResponse registrar(ProductoRequest request) {
        log.info("Registrando nuevo producto...");
        Producto producto= productoMapper.requestAEntidad(
                request, Categoria.obtenerCategoriaPorDescripcion(request.categoria().trim()));
        productoRepository.saveAndFlush(producto);
        log.info("nuevo producto {} registrado", producto.getNombre());
        return productoMapper.entidadAResponse(producto);
    }

    @Override
    public ProductoResponse actualizar(ProductoRequest request, Long id) {
        Producto producto = obtenerProductoOException(id);
        log.info("actualizando producto con id: {}", id);

        producto.actualizar(
                request.nombre().trim(),
                Categoria.obtenerCategoriaPorDescripcion(request.categoria().trim()),
                request.precio(),
                request.cantidad());
        productoRepository.saveAndFlush(producto);
        log.info("producto con id {} actualizado", id);

        return productoMapper.entidadAResponse(producto);
    }

    @Override
    public void eliminar(Long id) {
    Producto producto = obtenerProductoOException(id);
    log.info("Eliminando producto con id {}", id);
    productoRepository.delete(producto);
    productoRepository.flush();
    log.info("producto con id {} eliminado", id);
    }

    private Producto obtenerProductoOException(Long id){
        log.info("Buscando producto con id: {}", id);

        return productoRepository.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("Producto no encontrado con id: " + id)
        );
    }
}
