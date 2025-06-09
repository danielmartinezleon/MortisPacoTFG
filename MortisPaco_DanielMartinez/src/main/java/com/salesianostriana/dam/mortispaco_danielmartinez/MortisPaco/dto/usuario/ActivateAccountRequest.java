package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.dto.usuario;

import jakarta.validation.constraints.NotBlank;

public record ActivateAccountRequest(
        @NotBlank
        String token
) {
}
