package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LineaVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_linea_producto"))
    private Producto producto;

    private int cantidad;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_linea_venta"))
    private Venta venta;

    public double getTotalLinea() {
        return producto.getPrecio() * cantidad;
    }

}
