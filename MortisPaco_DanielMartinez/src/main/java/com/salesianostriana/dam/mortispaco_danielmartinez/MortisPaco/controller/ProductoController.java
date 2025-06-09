package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.controller;

import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.dto.producto.CreateProductoCmd;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.dto.producto.EditProductoCmd;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.dto.producto.GetProductoDto;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.dto.ventas.GetVentaDto;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.model.Producto;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.query.ProductoSpecificationBuilder;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.service.ProductoService;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.util.SearchCriteria;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/producto")
@Tag(name = "Productos", description = "Gestión de productos dentro del sistema")
public class ProductoController {

    private final ProductoService productoService;

    @Operation(summary = "Obtener todos los productos paginados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetProductoDto.class),
                            examples = @ExampleObject(value = """
                                    [
                                        {
                                            "id": "550e8400-e29b-41d4-a716-446655440000",
                                            "nombre": "Producto 1",
                                            "descripcion": "Descripción del producto 1",
                                            "precio": 99.99,
                                            "imageUrl": "http://example.com/image1.jpg"
                                        },
                                        {
                                            "id": "550e8400-e29b-41d4-a716-446655440001",
                                            "nombre": "Producto 2",
                                            "descripcion": "Descripción del producto 2",
                                            "precio": 199.99,
                                            "imageUrl": "http://example.com/image2.jpg"
                                        }
                                    ]
                                    """)))
    })
    @GetMapping("/lista")
    public ResponseEntity<Page<GetProductoDto>> getAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Producto> productos = productoService.findAll(pageable);
        Page<GetProductoDto> productosDto = productos.map(p -> GetProductoDto.of(p, getImageUrl(p.getImagen())));
        return ResponseEntity.ok(productosDto);
    }

    @Operation(summary = "Obtener un producto por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetProductoDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": "550e8400-e29b-41d4-a716-446655440000",
                                        "nombre": "Producto 1",
                                        "descripcion": "Descripción del producto 1",
                                        "precio": 99.99,
                                        "imageUrl": "http://example.com/image1.jpg"
                                    }
                                    """))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
    })
    @GetMapping("/{idProd}")
    public ResponseEntity<GetProductoDto> findById(@PathVariable UUID idProd) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(GetProductoDto.of(productoService.findById(idProd),
                        getImageUrl(productoService.findById(idProd).getImagen())));
    }

    @Operation(summary = "Crear un nuevo producto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetProductoDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": "550e8400-e29b-41d4-a716-446655440000",
                                        "nombre": "Nuevo Producto",
                                        "descripcion": "Descripción del nuevo producto",
                                        "precio": 150.00,
                                        "imageUrl": "http://example.com/newproduct.jpg"
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos para crear el producto",
                    content = @Content)
    })
    @PostMapping("/crear")
    public ResponseEntity<GetProductoDto> createProducto(
            @RequestPart("producto") @Valid CreateProductoCmd cmd,
            @RequestPart("file") MultipartFile file
    ) {
        Producto prod = productoService.createProducto(cmd, file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GetProductoDto.of(prod, getImageUrl(prod.getImagen())));
    }

    @Operation(summary = "Editar un producto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto editado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetProductoDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": "550e8400-e29b-41d4-a716-446655440000",
                                        "nombre": "Producto Editado",
                                        "descripcion": "Descripción actualizada",
                                        "precio": 120.00,
                                        "imageUrl": "http://example.com/editedproduct.jpg"
                                    }
                                    """))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
    })
    @PutMapping("/editar/{id}")
    public ResponseEntity<GetProductoDto> editProducto(
            @PathVariable UUID id,
            @RequestPart("productoEditable") @Valid EditProductoCmd cmd,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        return productoService.editProducto(id, cmd, file)
                .map(prod -> ResponseEntity.status(HttpStatus.OK)
                        .body(GetProductoDto.of(prod, getImageUrl(prod.getImagen()))))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar un producto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> deleteProducto(@PathVariable UUID id) {
        productoService.deleteProducto(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar productos por parámetros (categoría, precio mínimo, precio máximo)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos encontrados correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetProductoDto.class),
                            examples = @ExampleObject(value = """
                                    [
                                        {
                                            "id": "550e8400-e29b-41d4-a716-446655440000",
                                            "nombre": "Producto Filtrado 1",
                                            "descripcion": "Descripción del producto filtrado 1",
                                            "precio": 80.00,
                                            "imageUrl": "http://example.com/filteredproduct1.jpg"
                                        }
                                    ]
                                    """))),
            @ApiResponse(responseCode = "400", description = "Parámetros de búsqueda incorrectos", content = @Content)
    })
    @GetMapping("/buscar/")
    public ResponseEntity<Page<GetProductoDto>> buscar(
            @RequestParam(value = "categoria", required = false) String categoria,
            @RequestParam(value = "precioMin", required = false) Double precioMin,
            @RequestParam(value = "precioMax", required = false) Double precioMax,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        List<SearchCriteria> params = new ArrayList<>();

        if (categoria != null) {
            params.add(new SearchCriteria("categoria", ":", categoria));
        }
        if (precioMin != null) {
            params.add(new SearchCriteria("precioMin", ">", precioMin.toString()));
        }
        if (precioMax != null) {
            params.add(new SearchCriteria("precioMax", "<", precioMax.toString()));
        }

        ProductoSpecificationBuilder builder = new ProductoSpecificationBuilder(params);
        Specification<Producto> spec = builder.build();

        Pageable pageable = PageRequest.of(page, size);
        Page<Producto> productos = productoService.search(spec, pageable);

        Page<GetProductoDto> productosDto = productos.map(p -> GetProductoDto.of(p, getImageUrl(p.getImagen())));

        return ResponseEntity.ok(productosDto);
    }

    @Operation(summary = "Agregar un producto al carrito de compras")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto agregado al carrito correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetVentaDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": "550e8400-e29b-41d4-a716-446655440000",
                                        "usuarioId": "user123",
                                        "productos": [{"productoId": "550e8400-e29b-41d4-a716-446655440000", "cantidad": 2}]
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Error al agregar producto al carrito", content = @Content)
    })
    @PostMapping("/agregar/{productoId}")
    public ResponseEntity<GetVentaDto> agregarProductoAlCarrito(
            @RequestParam UUID usuarioId,
            @PathVariable UUID productoId,
            @RequestParam int cantidad) {

        GetVentaDto ventaActualizada = productoService.agregarProductoAlCarrito(usuarioId, productoId, cantidad);
        return ResponseEntity.ok(ventaActualizada);
    }


    public String getImageUrl(String filename) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(filename)
                .toUriString();
    }
}
