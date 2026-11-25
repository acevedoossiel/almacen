package com.ossiel.almacen.mappers;

import com.ossiel.almacen.dto.sucursales.SucursalRequest;
import com.ossiel.almacen.dto.sucursales.SucursalResponse;
import com.ossiel.almacen.entities.Sucursal;
import org.springframework.stereotype.Component;

@Component
public class SucursalMapper {
    public Sucursal requestAEntidad(SucursalRequest request){
        return request == null
                ? null
                : Sucursal.crear(request.nombre(), request.direccion());
    }

    public SucursalResponse entidadAResponse(Sucursal sucursal){
        return sucursal == null
                ? null
                : new SucursalResponse(sucursal.getId(),
                sucursal.getNombre(),
                sucursal.getDireccion()
        );
    }
}

