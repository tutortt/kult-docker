package com.kult.tienda.dto;

/** Identidad devuelta tras validar las credenciales. */
public record LoginResponse(
        Integer idUsuario,
        String nombre,
        String identificacion,
        String correo,
        String rol,
        String mensaje) {
}
