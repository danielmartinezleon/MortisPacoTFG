package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.dto.ventas;

import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.model.Venta;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record GetVentaDto(
        UUID id,
        LocalDate fecha,
        double importeTotal,
        double gastosEnvio,
        boolean abierta,
        List<GetLineaVentaDto> lineas
) {
    public static GetVentaDto of(Venta v) {
        return new GetVentaDto(
                v.getId(),
                v.getFecha(),
                v.getImporteTotal(),
                v.getGastosEnvio(),
                v.isAbierta(),
                GetLineaVentaDto.of(v.getLineas())
        );
    }
}
