package com.ossiel.almacen.entities;

import com.ossiel.almacen.utils.StringCustomUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SUCURSALES")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Sucursal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SUCURSAL")
    private Long id;

    @Column(name = "NOMBRE", length = 50, nullable = false, unique = true)
    private String nombre;

    @Column(name = "DIRECCION", length = 150, nullable = false)
    private String direccion;

    public static void validarDatos(String nombre, String direccion){
        StringCustomUtils.validarTamanio(nombre, 5,50,
                "La direccion es requerida y debe tener entre 10 y 150 caracteres");
    }

    public void actualizar(String nombre, String direccion){
        validarDatos(nombre, direccion);
        this.nombre= nombre.trim();
        this.direccion=direccion.trim();
    }

    public static Sucursal crear(String nombre, String direccion){
        validarDatos(nombre,direccion);
        return Sucursal.builder()
                .nombre(nombre.trim())
                .direccion(direccion.trim())
                .build();
    }
}
