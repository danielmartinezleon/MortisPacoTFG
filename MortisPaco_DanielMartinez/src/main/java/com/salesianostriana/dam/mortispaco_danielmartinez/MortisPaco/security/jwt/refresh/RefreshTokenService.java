package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.security.jwt.refresh;

import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.dto.usuario.UserResponse;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.model.Usuario;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.repository.UsuarioRepository;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.security.jwt.access.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    @Value("${jwt.refresh.duration}")
    private int durationInMinutes;

    public RefreshToken create(Usuario u) {
        refreshTokenRepository.deleteByUsuario(u);
        return refreshTokenRepository.save(
                RefreshToken.builder()
                        .usuario(u)
                        .expireAt(Instant.now().plusSeconds(durationInMinutes*60))
                        .build()
        );

    }

    public RefreshToken verify(RefreshToken refreshToken) {
        if (refreshToken.getExpireAt().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenException("Token expired");
        }

        return refreshToken;
    }

    public UserResponse refreshToken(String token) {
        return refreshTokenRepository.findById(UUID.fromString(token))
                .map(this::verify)
                .map(RefreshToken::getUsuario)
                .map(usuario -> {
                    String accessToken = jwtService.generateAccessToken(usuario);
                    RefreshToken refreshedToken = this.create(usuario);
                    return UserResponse.of(usuario, accessToken, refreshedToken.getToken());
                })
                .orElseThrow(() -> new RefreshTokenException("No se pudo refrescar el token"));
    }

    public void deleteByUser(Usuario user) {
        refreshTokenRepository.deleteByUsuario(user);
    }

}
