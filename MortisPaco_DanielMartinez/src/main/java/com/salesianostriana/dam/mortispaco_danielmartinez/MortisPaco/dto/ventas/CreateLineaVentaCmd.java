package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.dto.ventas;

import java.util.UUID;

public record CreateLineaVentaCmd(
        UUID productoId,
        int cantidad
) {}
