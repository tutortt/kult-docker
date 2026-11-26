package com.kult.tienda.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(nullable = false, unique = true, length = 150)
    private String correo;

    @Column(length = 20)
    private String celular;

    @Column(unique = true, length = 20)
    private String documento;

    @JsonIgnore
    @Column(nullable = false, length = 100)
    private String passwordHash;

    @Column(length = 60)
    private String ciudad;

    @Column(length = 5)
    private String talla;

    @Column(nullable = false)
    private boolean aceptaSms;

    @Column(nullable = false)
    private boolean aceptaTerminos;

    @Column(nullable = false, length = 20, columnDefinition = "VARCHAR(20) DEFAULT 'CLIENTE'")
    private String rol = "CLIENTE";

    @JsonIgnore
    @OneToMany(mappedBy = "usuario")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Direccion> direcciones = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "usuario")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Pedido> pedidos = new ArrayList<>();
}
