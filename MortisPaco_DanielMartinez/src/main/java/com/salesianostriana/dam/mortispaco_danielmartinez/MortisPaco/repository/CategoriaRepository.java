package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.repository;

import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {
}
