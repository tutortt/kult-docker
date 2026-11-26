package com.kult.tienda.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/** Respuesta de una compra simulada confirmada. */
public record CheckoutResponse(
        Integer idPedido,
        String estadoPedido,
        String estadoPago,
        String medioPago,
        LocalDateTime fecha,
        BigDecimal subtotal,
        BigDecimal costoEnvio,
        BigDecimal total,
        Integer cantidadArticulos,
        List<CheckoutItemResponse> items,
        String mensaje) {
}
