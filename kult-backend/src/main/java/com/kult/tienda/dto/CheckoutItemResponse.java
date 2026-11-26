package com.kult.tienda.dto;

import java.math.BigDecimal;

/** Línea de compra confirmada, con precio calculado por el backend. */
public record CheckoutItemResponse(
        Integer idProducto,
        Integer idVariante,
        String producto,
        String talla,
        String color,
        String imagenUrl,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal) {
}
