package com.kult.tienda.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/** Credenciales enviadas por la pantalla de acceso. */
public record LoginRequest(
        @NotBlank(message = "El correo es obligatorio") @Email(message = "El correo no es válido") String email,
        @NotBlank(message = "La contraseña es obligatoria") String password,
        boolean rememberSession) {
}
