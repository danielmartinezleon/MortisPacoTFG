package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.dto.usuario;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank
        String username,
        @NotBlank
        String password
) {
}
