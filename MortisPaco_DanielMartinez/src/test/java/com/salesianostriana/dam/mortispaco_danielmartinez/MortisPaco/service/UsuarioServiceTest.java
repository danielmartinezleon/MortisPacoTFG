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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SendGridMailSender mailSender;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .id(UUID.randomUUID())
                .username("testuser")
                .password("password")
                .email("test@example.com")
                .roles(Set.of(Role.USER))
                .enabled(false)
                .activationToken("token123")
                .build();
    }

    @Test
    void testCreateUser() throws IOException {
        CreateUserCmd cmd = new CreateUserCmd("testuser", "password", "test@example.com", "Test", "User", "Address");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        Usuario result = usuarioService.createUser(cmd);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(mailSender).sendMail(eq("test@example.com"), anyString(), anyString());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void testActivateAccount_Success() {
        when(usuarioRepository.findByActivationToken("token123")).thenReturn(Optional.of(usuario));
        usuario.setCreatedAt(usuario.getCreatedAt().minusSeconds(60));

        Usuario activatedUser = usuarioService.activateAccount("token123");

        assertNotNull(activatedUser);
        assertTrue(activatedUser.isEnabled());
        assertNull(activatedUser.getActivationToken());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void testActivateAccount_Failure() {
        when(usuarioRepository.findByActivationToken("invalid_token")).thenReturn(Optional.empty());

        assertThrows(ActivationExpiredException.class, () -> usuarioService.activateAccount("invalid_token"));
    }

    @Test
    void testEditUser() {
        UUID userId = usuario.getId();
        EditUserCmd editCmd = new EditUserCmd("Updated", "User", "updated@example.com", "newpassword", "New Address");
        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Optional<Usuario> result = usuarioService.editUser(userId, editCmd);

        assertTrue(result.isPresent());
        assertEquals("Updated", result.get().getNombre());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void testGetVentasCerradasPorUsuario() {
        UUID userId = usuario.getId();
        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(usuario));
        when(ventaRepository.findByClienteAndAbiertaFalse(usuario)).thenReturn(List.of());

        List<GetVentaDto> result = usuarioService.getVentasCerradasPorUsuario(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
