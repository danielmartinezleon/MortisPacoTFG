package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.dto.usuario;

import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.model.Usuario;

import java.util.UUID;

public record GetUserDto(
        UUID id,
        String username,
        String nombre,
        String apellidos,
        String email,
        String direccion

) {
    public static GetUserDto of(Usuario u) {
        return new GetUserDto(
                u.getId(),
                u.getUsername(),
                u.getNombre(),
                u.getApellidos(),
                u.getEmail(),
                u.getDireccion()
        );
    }
}
