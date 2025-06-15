package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.controller;

import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.dto.ventas.GetVentaDto;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.model.Usuario;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.service.ProductoService;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carrito")
@Tag(name = "Carro", description = "Gestión de carro (finaliza compra o elimina productos")
public class CarritoController {

    private final ProductoService productoService;

    @Operation(summary = "Cerrar la venta del carrito para un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venta cerrada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetVentaDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": "550e8400-e29b-41d4-a716-446655440000",
                                        "total": 200.00
                                    }
                                    """))),
            @ApiResponse(responseCode = "404", description = "Usuario o venta no encontrados", content = @Content),
            @ApiResponse(responseCode = "400", description = "Error al cerrar la venta", content = @Content)
    })
    @PutMapping("/cerrarventa/{ventaId}")
    public ResponseEntity<GetVentaDto> cerrarVenta(@AuthenticationPrincipal Usuario usuario, @PathVariable UUID ventaId) {
        try {
            GetVentaDto ventaCerrada = productoService.cerrarVentaParaUsuario(usuario, ventaId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ventaCerrada);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Operation(summary = "Eliminar un producto del carrito de compras")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto eliminado del carrito correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetVentaDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": "550e8400-e29b-41d4-a716-446655440000",
                                        "usuarioId": "user123",
                                        "productos": []
                                    }
                                    """))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado en el carrito", content = @Content)
    })
    @DeleteMapping("/eliminar/{lineaVentaId}")
    public ResponseEntity<GetVentaDto> eliminarProductoDelCarrito(
            @AuthenticationPrincipal Usuario usuario,
            @PathVariable UUID lineaVentaId) {
        try {
            GetVentaDto ventaActualizada = productoService.eliminarProductoDelCarrito(usuario, lineaVentaId);
            return ResponseEntity.ok(ventaActualizada);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }


    @Operation(summary = "Obtener el carrito (venta abierta) de un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carrito obtenido correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetVentaDto.class))),
            @ApiResponse(responseCode = "404", description = "Usuario o carrito no encontrado", content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<GetVentaDto> obtenerCarrito(@AuthenticationPrincipal Usuario usuario) {
        try {
            GetVentaDto carrito = productoService.obtenerCarrito(usuario);
            return ResponseEntity.ok(carrito);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
