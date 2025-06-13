package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.dto.usuario;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.model.Usuario;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String token,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String refreshToken,

        String userRole

) {

    public static UserResponse of (Usuario user) {
        return new UserResponse(user.getId(), user.getUsername(), null, null,
                user.getRoles().stream().findFirst().get().toString());
    }

    public static UserResponse of (Usuario user, String token, String refreshToken) {
        return new UserResponse(user.getId(), user.getUsername(), token, refreshToken,
                user.getRoles().stream().findFirst().get().toString());
    }

}