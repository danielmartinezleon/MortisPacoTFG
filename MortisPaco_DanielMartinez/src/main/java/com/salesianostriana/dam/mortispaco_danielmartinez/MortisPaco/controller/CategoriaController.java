package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.controller;

import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.dto.categoria.CreateCategoriaCmd;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.dto.categoria.EditCategoriaCmd;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.dto.categoria.GetCategoriaDto;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.model.Categoria;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categoria")
@Tag(name = "Categorías", description = "Gestión de categorías dentro del sistema")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @Operation(summary = "Obtener todas las categorías")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetCategoriaDto.class),
                            examples = @ExampleObject(value = """
                                    [
                                        {
                                            "id": "550e8400-e29b-41d4-a716-446655440000",
                                            "nombre": "Electrónica"
                                        },
                                        {
                                            "id": "550e8400-e29b-41d4-a716-446655440001",
                                            "nombre": "Ropa"
                                        }
                                    ]
                                    """)))
    })
    @GetMapping("/lista")
    public ResponseEntity<List<GetCategoriaDto>> getAll() {
        return ResponseEntity.ok(
                categoriaService.findAll()
                        .stream()
                        .map(GetCategoriaDto::of)
                        .toList()
        );
    }

    @Operation(summary = "Crear una nueva categoría")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoría creada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Categoria.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": "550e8400-e29b-41d4-a716-446655440002",
                                        "nombre": "Deportes"
                                    }
                                    """)))
    })
    @PostMapping("/crear")
    public ResponseEntity<Categoria> createCategoria(@RequestBody @Valid CreateCategoriaCmd cmd) {
        Categoria categoria = categoriaService.createCategoria(cmd);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoria);
    }

    @Operation(summary = "Editar una categoría existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría editada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Categoria.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": "550e8400-e29b-41d4-a716-446655440003",
                                        "nombre": "Hogar"
                                    }
                                    """))),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada",
                    content = @Content)
    })
    @PutMapping("/editar/{id}")
    public ResponseEntity<Categoria> editCategoria(@PathVariable UUID id, @RequestBody @Valid EditCategoriaCmd cmd) {
        return categoriaService.editCategoria(id, cmd)
                .map(categoria -> ResponseEntity.status(HttpStatus.OK).body(categoria))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar una categoría")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoría eliminada correctamente",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada",
                    content = @Content)
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> deleteCategoria(@PathVariable UUID id) {
        categoriaService.deleteCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
