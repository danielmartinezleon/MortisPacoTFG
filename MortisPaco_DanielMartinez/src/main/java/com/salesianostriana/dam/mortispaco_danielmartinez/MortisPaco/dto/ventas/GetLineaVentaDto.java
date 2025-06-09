package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.dto.ventas;

import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.model.LineaVenta;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record GetLineaVentaDto(
        UUID productoId,
        String nombreProducto,
        int cantidad,
        double totalLinea
) {
    public static GetLineaVentaDto of(LineaVenta lv) {
        return new GetLineaVentaDto(
                lv.getId(),
                lv.getProducto().getNombre(),
                lv.getCantidad(),
                lv.getTotalLinea()
        );
    }

    public static List<GetLineaVentaDto> of(List<LineaVenta> lineas) {
        return lineas.stream()
                .map(lv -> new GetLineaVentaDto(
                        lv.getId(),
                        lv.getProducto().getNombre(),
                        lv.getCantidad(),
                        lv.getTotalLinea()
                ))
                .collect(Collectors.toList());
    }
}
