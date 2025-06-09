package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nombre;
    private String descripcion;
    private int stock;
    private double precio;
    private String imagen;
    private boolean descuento;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    @ToString.Exclude
    private Categoria categoria;

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "nombre = " + nombre + ", " +
                "descripcion = " + descripcion + ", " +
                "stock = " + stock + ", " +
                "precio = " + precio + ", " +
                "imagen = " + imagen + ", " +
                "descuento = " + descuento + ", " +
                "categoria = " + categoria + ")";
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Producto producto = (Producto) o;
        return getNombre() != null && Objects.equals(getNombre(), producto.getNombre());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(nombre);
    }
}
