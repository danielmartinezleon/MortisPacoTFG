package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.controller;

import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.dto.ventas.GetVentaDto;
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
    @PutMapping("/cerrarventa/{usuarioId}/{ventaId}")
    public ResponseEntity<GetVentaDto> cerrarVenta(@PathVariable UUID usuarioId, @PathVariable UUID ventaId) {
        try {
            GetVentaDto ventaCerrada = productoService.cerrarVentaParaUsuario(usuarioId, ventaId);
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
    @DeleteMapping("/eliminar/{ventaId}/{lineaVentaId}")
    public ResponseEntity<GetVentaDto> eliminarProductoDelCarrito(
            @PathVariable UUID ventaId,
            @PathVariable UUID lineaVentaId) {
        try {
            GetVentaDto ventaActualizada = productoService.eliminarProductoDelCarrito(ventaId, lineaVentaId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ventaActualizada);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    @Operation(summary = "Obtener el carrito (venta abierta) de un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carrito obtenido correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetVentaDto.class))),
            @ApiResponse(responseCode = "404", description = "Usuario o carrito no encontrado", content = @Content)
    })
    @GetMapping("/{usuarioId}")
    public ResponseEntity<GetVentaDto> obtenerCarrito(@PathVariable UUID usuarioId) {
        try {
            GetVentaDto carrito = productoService.obtenerCarrito(usuarioId);
            return ResponseEntity.ok(carrito);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
