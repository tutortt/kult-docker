package com.kult.tienda.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** Ítem del carrito enviado al checkout simulado. */
public record CheckoutItemRequest(
        @NotNull(message = "La variante es obligatoria") Integer idVariante,
        @NotNull(message = "La cantidad es obligatoria") @Min(value = 1, message = "La cantidad debe ser mayor a 0") Integer cantidad) {
}
