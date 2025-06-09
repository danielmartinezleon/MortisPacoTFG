package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NaturalId
    @Column(unique = true, nullable = false)
    private String username;

    private String password;
    private String nombre;
    private String apellidos;
    private String email;
    private String direccion;


    @OneToMany(mappedBy = "cliente")
    @Builder.Default
    private List<Venta> ventas = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @Builder.Default
    private boolean enabled = false;

    private String activationToken;

    @Builder.Default
    private Instant createdAt = Instant.now();


    public void addVenta(Venta lv) {
        this.ventas.add(lv);
        lv.setCliente(this);
    }

    public void removeVenta(Venta lv) {
        this.ventas.remove(lv);
        lv.setCliente(null);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }


}
