package com.kult.tienda.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "sedes")
public class Sede {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSede;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String ciudad;

    @Column(nullable = false, length = 255)
    private String direccion;

    @Column(nullable = false)
    private Boolean activa = true;
}
