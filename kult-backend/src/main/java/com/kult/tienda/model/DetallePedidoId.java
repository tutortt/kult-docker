package com.kult.tienda.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.Data;

@Data
@Embeddable
public class DetallePedidoId implements Serializable {
    @Column(name = "id_pedido")
    private Integer idPedido;

    @Column(name = "id_variante")
    private Integer idVariante;
}
