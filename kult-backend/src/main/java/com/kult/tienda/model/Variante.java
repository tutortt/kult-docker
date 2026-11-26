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
@Table(name = "variantes")
public class Variante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idVariante;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_producto", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Producto producto;

    @Column(nullable = false, length = 10)
    private String talla;

    @Column(nullable = false, length = 50)
    private String color;

    @Column(nullable = false)
    private Integer stock = 0;

    @JsonIgnore
    @OneToMany(mappedBy = "variante")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<DetallePedido> detallesPedido = new ArrayList<>();
}
