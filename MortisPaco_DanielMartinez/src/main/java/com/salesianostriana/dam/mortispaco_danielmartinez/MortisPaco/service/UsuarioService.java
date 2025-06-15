package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.service;

import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.dto.usuario.CreateUserCmd;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.dto.usuario.EditUserCmd;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.dto.ventas.GetVentaDto;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.error.ActivationExpiredException;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.model.Role;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.model.Usuario;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.model.Venta;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.repository.UsuarioRepository;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.repository.VentaRepository;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.util.SendGridMailSender;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final VentaRepository ventaRepository;
    private final PasswordEncoder passwordEncoder;
    private final SendGridMailSender mailSender;

    @Value("${activation.duration}")
    private int activationDuration;

    public Usuario findById(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Usuario createUser(CreateUserCmd createUserCmd) {
        Usuario usuario = Usuario.builder()
                .username(createUserCmd.username())
                .password(passwordEncoder.encode(createUserCmd.password()))
                .email(createUserCmd.email())
                .nombre(createUserCmd.nombre())
                .apellidos(createUserCmd.apellidos())
                .direccion(createUserCmd.direccion())
                .activationToken(generateRandomActivationCode())
                .roles(Set.of(Role.USER))
                .build();

        try {
            mailSender.sendMail(createUserCmd.email(), "Activación de cuenta", "Bienvenido a MortisPaco, tu tienda de LARP, aquí tiene su" +
                    "código de activación de cuenta:\n\n"+usuario.getActivationToken());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se ha podido enviar el email de activación, vuelva a intentarlo más tarde");
        }

        return usuarioRepository.save(usuario);
    }

    public Usuario createAdmin(CreateUserCmd createUserCmd) {
        Usuario usuario = Usuario.builder()
                .username(createUserCmd.username())
                .password(passwordEncoder.encode(createUserCmd.password()))
                .email(createUserCmd.email())
                .nombre(createUserCmd.nombre())
                .apellidos(createUserCmd.apellidos())
                .direccion(createUserCmd.direccion())
                .activationToken(generateRandomActivationCode())
                .roles(Set.of(Role.ADMIN))
                .build();

        try {
            mailSender.sendMail(createUserCmd.email(), "Activación de cuenta", "Bienvenido a MortisPaco, tu tienda de LARP, aquí tiene su" +
                    "código de activación de cuenta como Administrador:\n\n"+usuario.getActivationToken());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se ha podido enviar el email de activación, vuelva a intentarlo más tarde");
        }

        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> editUser(UUID id, EditUserCmd editUserCmd) {
        return usuarioRepository.findById(id).map(user -> {
            user.setNombre(editUserCmd.nombre());
            user.setApellidos(editUserCmd.apellidos());
            user.setEmail(editUserCmd.email());
            user.setPassword(passwordEncoder.encode(editUserCmd.password()));
            user.setDireccion(editUserCmd.direccion());

            return usuarioRepository.save(user);
        });
    }

    public String generateRandomActivationCode() {
        return UUID.randomUUID().toString();
    }

    public Usuario activateAccount(String token) {
        return usuarioRepository.findByActivationToken(token)
                .filter(user -> ChronoUnit.MINUTES.between(Instant.now(), user.getCreatedAt()) - activationDuration < 0)
                .map(user -> {
                    user.setEnabled(true);
                    user.setActivationToken(null);
                    return usuarioRepository.save(user);
                })
                .orElseThrow(() -> new ActivationExpiredException("El código de activación no existe o ha caducado"));
    }

    @Transactional
    public List<GetVentaDto> getVentasCerradas(Usuario usuarioAuth) {
        Usuario usuario = usuarioRepository.findById(usuarioAuth.getId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        List<Venta> ventasCerradas;

        if (usuario.getRoles().contains(Role.ADMIN)) {
            ventasCerradas = ventaRepository.findByAbiertaFalse();
        } else {
            ventasCerradas = ventaRepository.findByClienteAndAbiertaFalse(usuario);
        }

        return ventasCerradas.stream()
                .map(GetVentaDto::of)
                .toList();
    }


    @Transactional
    public List<GetVentaDto> getVentasCerradasPorUsuario(UUID usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        List<Venta> ventasCerradas = ventaRepository.findByClienteAndAbiertaFalse(usuario);
        return ventasCerradas.stream()
                .map(GetVentaDto::of)
                .toList();
    }
}
