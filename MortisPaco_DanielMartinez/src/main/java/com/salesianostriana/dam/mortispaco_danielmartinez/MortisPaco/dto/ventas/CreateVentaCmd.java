package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.dto.ventas;

import java.util.List;
import java.util.UUID;

public record CreateVentaCmd(
        List<UUID> productosIds,
        double gastosEnvio
) {}
