package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.dto.producto;

import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.model.Producto;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.validation.UniqueNombre;

import java.util.UUID;

public record GetProductoDto(
        UUID id,
        String nombre,
        String descripcion,
        int stock,
        double precio,
        boolean descuento,
        UUID categoriaId,
        String imageUrl
) {
    public static GetProductoDto of(Producto p, String url){
        return new GetProductoDto(
                p.getId(),
                p.getNombre(),
                p.getDescripcion(),
                p.getStock(),
                p.getPrecio(),
                p.isDescuento(),
                p.getCategoria().getId(),
                url
        );
    }
}

