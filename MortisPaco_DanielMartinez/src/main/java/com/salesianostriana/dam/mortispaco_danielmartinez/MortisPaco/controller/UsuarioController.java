package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.controller;

import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.dto.usuario.*;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.dto.ventas.GetVentaDto;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.model.Role;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.model.Usuario;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.security.jwt.access.JwtService;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.security.jwt.refresh.RefreshToken;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.security.jwt.refresh.RefreshTokenRequest;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.security.jwt.refresh.RefreshTokenService;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/usuario")
@Tag(name = "Usuarios", description = "Gestión de usuarios en el sistema")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Operation(summary = "Registrar un nuevo usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": "550e8400-e29b-41d4-a716-446655440000",
                                        "username": "usuario123"
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos para crear el usuario", content = @Content)
    })
    @PostMapping("/auth/registerUser")
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid CreateUserCmd createUserCmd) {
        Usuario user = usuarioService.createUser(createUserCmd);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.of(user));
    }

    @Operation(summary = "Registrar un nuevo administrador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Administrador creado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": "550e8400-e29b-41d4-a716-446655440000",
                                        "username": "admin123"
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos para crear el administrador", content = @Content)
    })
    @PostMapping("/auth/registerAdmin")
    public ResponseEntity<UserResponse> registerAdmin(@RequestBody @Valid CreateUserCmd createUserCmd) {
        Usuario user = usuarioService.createAdmin(createUserCmd);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.of(user));
    }

    @Operation(summary = "Iniciar sesión como usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Login exitoso, devuelve tokens de acceso y actualización",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": "550e8400-e29b-41d4-a716-446655440000",
                                        "username": "usuario123",
                                        "token": "access_token_string",
                                        "refreshToken": "refresh_token_string"
                                    }
                                    """))),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas", content = @Content)
    })
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        Usuario user = (Usuario) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.create(user);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth.getAuthorities());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.of(user, accessToken, refreshToken.getToken()));
    }

    @PostMapping("/auth/refresh/token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest req) {
        System.out.println("Received refresh token: " + req.refreshToken());
        String token = req.refreshToken();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(refreshTokenService.refreshToken(token));
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal Usuario user) {
        refreshTokenService.deleteByUser(user);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Editar datos de usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario editado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetUserDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": "550e8400-e29b-41d4-a716-446655440000",
                                        "username": "usuario123",
                                        "nombre": "Juan",
                                        "apellidos": "Pérez",
                                        "email": "juan.perez@example.com",
                                        "direccion": "Calle Ficticia 123"
                                    }
                                    """))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })
    @PutMapping("/editar/{id}")
    public ResponseEntity<GetUserDto> editUser(
            @PathVariable UUID id,
            @RequestBody @Valid EditUserCmd editUserCmd) {

        return usuarioService.editUser(id, editUserCmd)
                .map(user -> ResponseEntity.status(HttpStatus.OK)
                        .body(GetUserDto.of(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Activar cuenta de usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cuenta activada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": "550e8400-e29b-41d4-a716-446655440000",
                                        "username": "usuario123"
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Token de activación inválido", content = @Content)
    })
    @PostMapping("/activate/account/")
    public ResponseEntity<?> activateAccount(@RequestBody @Valid ActivateAccountRequest req) {
        String token = req.token();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.of(usuarioService.activateAccount(token)));
    }

    @Operation(summary = "Obtener ventas cerradas por usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ventas cerradas del usuario obtenidas correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetVentaDto.class),
                            examples = @ExampleObject(value = """
                                    [
                                        {
                                            "id": "550e8400-e29b-41d4-a716-446655440000",
                                            "total": 100.00
                                        }
                                    ]
                                    """))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })

    @GetMapping("/historial")
    public ResponseEntity<List<GetVentaDto>> getVentasCerradas(@AuthenticationPrincipal Usuario usuarioAuth) {
        try {
            List<GetVentaDto> ventas = usuarioService.getVentasCerradas(usuarioAuth);
            return ResponseEntity.ok(ventas);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



}
