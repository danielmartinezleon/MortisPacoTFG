package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.dto.categoria;

import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.validation.UniqueNombre;

public record CreateCategoriaCmd(

        @UniqueNombre(message = "Esa categoria ya existe")
        String nombre
) {
}
