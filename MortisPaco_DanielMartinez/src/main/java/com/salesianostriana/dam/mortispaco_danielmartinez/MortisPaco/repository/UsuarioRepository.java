package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.repository;

import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    Optional<Usuario> findFirstByUsername(String username);

    Optional<Usuario> findByActivationToken(String activationToken);

    boolean existsByUsername(String username);


}
