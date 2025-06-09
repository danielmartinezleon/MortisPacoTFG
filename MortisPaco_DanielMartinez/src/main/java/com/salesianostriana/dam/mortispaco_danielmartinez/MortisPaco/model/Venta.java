package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    private double importeTotal;

    private double gastosEnvio;

    private boolean abierta;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Usuario cliente;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "venta", cascade=CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<LineaVenta> lineas = new ArrayList<>();

    public void addVentaCliente(Usuario cliente) {
        this.cliente = cliente;
        cliente.getVentas().add(this);
    }

    public void removeLineaVenta(Usuario cliente) {
        this.cliente = null;
        cliente.getVentas().remove(this);
    }

    public void addLineaVenta(LineaVenta lineaVenta) {
        if (lineas == null) {
            lineas = new ArrayList<>();
        }
        lineas.add(lineaVenta);
        lineaVenta.setVenta(this);
    }


    public void removeLineaVenta(LineaVenta lineaVenta) {
        lineaVenta.setVenta(null);
        this.getLineas().remove(lineaVenta);
    }

    public void removeLineaVenta(UUID lineaVenta_id) {
        Optional<LineaVenta> lv = lineas.stream()
                .filter(x -> x.getId().equals(this.id) &&
                        x.getId().equals(lineaVenta_id))
                .findFirst();
        if (lv.isPresent()) {
            removeLineaVenta(lv.get());
        }
    }


    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "fecha = " + fecha + ", " +
                "importeTotal = " + importeTotal + ", " +
                "gastosEnvio = " + gastosEnvio + ", " +
                "abierta = " + abierta + ", " +
                "cliente = " + cliente + ")";
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Venta venta = (Venta) o;
        return getId() != null && Objects.equals(getId(), venta.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
