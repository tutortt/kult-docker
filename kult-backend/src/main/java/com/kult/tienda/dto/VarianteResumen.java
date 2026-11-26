package com.kult.tienda.dto;

/** Variante expuesta al catálogo/carrito para identificar talla, color y stock. */
public record VarianteResumen(Integer idVariante, String talla, String color, Integer stock) {
}
