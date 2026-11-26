package com.kult.tienda.dto;

/** Respuesta pública del registro. Nunca incluye el hash de la contraseña. */
public record RegistrationResponse(
        Integer idUsuario,
        String nombre,
        String identificacion,
        String correo,
        String ciudad,
        String talla,
        String mensaje) {
}
