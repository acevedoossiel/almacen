package com.ossiel.almacen.utils;

import com.ossiel.almacen.entities.Producto;
import com.ossiel.almacen.entities.Sucursal;
import com.ossiel.almacen.enums.Categoria;
import com.ossiel.almacen.repositories.ProductoRepository;
import com.ossiel.almacen.repositories.SucursalRepository;
import com.ossiel.almacen.repositories.VentaRepository;
import com.ossiel.almacen.services.ventas.VentaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DatosIniciales implements CommandLineRunner {
    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;
    private final VentaRepository ventaRepository;
    private final VentaService ventaService;
    @Override
    public void run(String... args) throws Exception {
        if (productoRepository.count() == 0){
            productoRepository.saveAll(
                    List.of(
                            new Producto(
                                    null,
                                    "Laptop Gamer",
                                    Categoria.ELECTRONICA,
                                    BigDecimal.valueOf(25),
                                    50
                            ),
                            new Producto(
                                    null,
                                    "Mouse Inalámbrico",
                                    Categoria.ELECTRONICA,
                                    BigDecimal.valueOf(25),
                                    50
                            ),
                            new Producto(
                                    null,
                                    "Camiseta deportiva",
                                    Categoria.ROPA,
                                    BigDecimal.valueOf(20),
                                    100
                            )
                    )
            );
        }
        log.info("Productos de prueba cargados correctamente");

        if (sucursalRepository.count() == 0){
            sucursalRepository.saveAll(
                    List.of(
                            new Sucursal(
                                    null,
                                    "Sucursal 1",
                                    "Direccion 1"
                            ),
                            new Sucursal(
                                    null,
                                    "Sucursal 2",
                                    "Direccion 2"
                            ),
                            new Sucursal(
                                    null,
                                    "Sucursal 3",
                                    "Direccion 3"
                            )
                    )
            );
        }
        log.info("Sucursales de prueba cargadas correctamente");
    }
}
