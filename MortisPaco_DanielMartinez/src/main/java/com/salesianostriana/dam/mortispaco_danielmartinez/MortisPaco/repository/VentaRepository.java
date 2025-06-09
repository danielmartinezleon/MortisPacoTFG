package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.repository;

import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.model.Usuario;
import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VentaRepository extends JpaRepository<Venta, UUID> {

    List<Venta> findByAbiertaFalse();
    List<Venta> findByClienteAndAbiertaFalse(Usuario cliente);
    Venta findByClienteAndAbiertaTrue(Usuario cliente);
}
