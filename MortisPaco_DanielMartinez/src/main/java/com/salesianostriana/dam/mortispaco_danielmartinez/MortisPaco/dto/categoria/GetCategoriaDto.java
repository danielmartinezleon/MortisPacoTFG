package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.dto.categoria;

import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.model.Categoria;

import java.util.UUID;

public record GetCategoriaDto(
        UUID id,
        String nombre
) {

    public static GetCategoriaDto of(Categoria categoria) {
        return new GetCategoriaDto(
                categoria.getId(),
                categoria.getNombre()
        );
    }
}
