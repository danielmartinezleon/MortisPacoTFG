package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.dto.usuario;

import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.validation.FieldsValueMatch;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.validation.UniqueUsername;

@FieldsValueMatch(
        field = "password",
        fieldMatch = "verifyPassword",
        message = "Los valores de password y verifyPassword no coinciden")
public record CreateUserCmd(

        @UniqueUsername
        String username,
        String email,
        String nombre,
        String apellidos,
        String direccion,
        String password,
        String verifyPassword
) {
}
