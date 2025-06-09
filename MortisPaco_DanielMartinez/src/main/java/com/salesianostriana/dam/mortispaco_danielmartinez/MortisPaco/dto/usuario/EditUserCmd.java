package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.dto.usuario;

import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.validation.FieldsValueMatch;

@FieldsValueMatch(
        field = "password",
        fieldMatch = "verifyPassword",
        message = "Los valores de password y verifyPassword no coinciden")
public record EditUserCmd(
        String email,
        String nombre,
        String apellidos,
        String direccion,
        String password
) {
}
