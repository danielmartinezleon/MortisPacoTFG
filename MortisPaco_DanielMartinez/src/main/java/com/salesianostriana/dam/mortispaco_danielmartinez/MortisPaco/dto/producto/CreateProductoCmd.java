package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.dto.producto;

import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.validation.UniqueNombre;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateProductoCmd(

        @NotBlank(message = "El nombre no puede estar vacio")
        @UniqueNombre(message = "Este producto ya existe")
        String nombre,
        String descripcion,
        @Min(1)
        int stock,
        @DecimalMin("0.01")
        double precio,
        boolean descuento,
        @NotNull
        UUID categoriaId
) {
}
