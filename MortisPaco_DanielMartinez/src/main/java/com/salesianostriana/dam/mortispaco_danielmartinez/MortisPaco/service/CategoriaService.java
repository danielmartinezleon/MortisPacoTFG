package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.service;

import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.dto.categoria.CreateCategoriaCmd;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.dto.categoria.EditCategoriaCmd;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.dto.producto.EditProductoCmd;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.model.Categoria;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.repository.CategoriaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public Categoria createCategoria(CreateCategoriaCmd cmd) {
        Categoria categoria = Categoria.builder()
                .nombre(cmd.nombre())
                .build();

        return categoriaRepository.save(categoria);
    }

    @Transactional
    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> editCategoria(UUID id, EditCategoriaCmd cmd) {
        return categoriaRepository.findById(id).map(categoria -> {
            categoria.setNombre(cmd.nombre());
            return categoriaRepository.save(categoria);
        });
    }

    public void deleteCategoria(UUID id) {
        categoriaRepository.deleteById(id);
    }
}
