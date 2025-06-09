package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.repository;

import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface ProductoRepository extends JpaRepository<Producto, UUID>,
        JpaSpecificationExecutor<Producto> {

    Page<Producto> findAll(Specification<Producto> spec, Pageable pageable);

    boolean existsByNombre(String nombre);

    Optional<Producto> findByNombre(String nombre);
}
