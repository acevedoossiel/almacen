package com.ossiel.almacen.services.ventas;

import com.ossiel.almacen.dto.ventas.DetalleVentaResponse;
import com.ossiel.almacen.mappers.DetalleVentaMapper;
import com.ossiel.almacen.repositories.DetalleVentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DetalleVentaServiceImpl implements DetalleVentaService {

    private final DetalleVentaRepository detalleVentaRepository;
    private final DetalleVentaMapper detalleVentaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVentaResponse> listar() {
        log.info("Listando detalles de venta");
        return detalleVentaRepository.findAll().stream()
                .map(detalleVentaMapper::entidadAResponse)
                .toList();
    }
}
