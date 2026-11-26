package com.kult.tienda.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

/** Solicitud de compra simulada. Calcula totales y actualiza inventario en servidor. */
public record CheckoutRequest(
        @NotBlank(message = "El nombre es obligatorio") @Size(max = 120) String nombre,
        @NotBlank(message = "El correo es obligatorio") @Size(max = 150) String correo,
        @NotBlank(message = "La dirección es obligatoria") @Size(max = 255) String direccion,
        @NotBlank(message = "La ciudad es obligatoria") @Size(max = 100) String ciudad,
        @NotBlank(message = "El departamento es obligatorio") @Size(max = 100) String departamento,
        @Size(max = 20) String codigoPostal,
        @NotBlank(message = "El medio de pago es obligatorio") @Size(max = 50) String medioPago,
        @NotEmpty(message = "El carrito no puede estar vacío") List<@Valid CheckoutItemRequest> items) {
}
