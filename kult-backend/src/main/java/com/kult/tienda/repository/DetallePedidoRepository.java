package com.kult.tienda.repository;

import com.kult.tienda.model.DetallePedido;
import com.kult.tienda.model.DetallePedidoId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, DetallePedidoId> {
}
