package com.ossiel.almacen.dto.sucursales;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Informacion de una sucursal")
public record SucursalResponse(
        @Schema(description = "ID unico de la sucursal", example = "1")
        Long id,

        @Schema(description = "Nombre de la sucursal", example = "Sucursal 1")
        String nombre,

        @Schema(description = "Direccion de la sucursal", example = "Colonia Hueyetiplan")
        String direccion

) {
}
