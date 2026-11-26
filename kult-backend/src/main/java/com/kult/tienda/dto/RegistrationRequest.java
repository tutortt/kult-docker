package com.kult.tienda.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;


/** Datos capturados en el formulario de registro de la tienda. */
public record RegistrationRequest(
        @NotBlank(message = "El nombre es obligatorio") @Size(max = 100) String nombre,
        @NotBlank(message = "El apellido es obligatorio") @Size(max = 100) String apellido,
        @NotBlank(message = "La identificación es obligatoria") @Pattern(regexp = "[A-Za-z0-9 .\\-]{6,30}", message = "El número de identificación no es válido") String identificacion,
        @NotBlank(message = "El correo es obligatorio") @Email(message = "El correo no es válido") @Size(max = 150) String correo,
        @NotBlank(message = "El móvil es obligatorio") @Pattern(regexp = "[+0-9 ()\\-]{7,20}", message = "El móvil no es válido") String celular,
        @NotBlank(message = "La ciudad es obligatoria") @Size(max = 60) String ciudad,
        @NotBlank(message = "La contraseña es obligatoria") @Size(min = 8, max = 100, message = "La contraseña debe tener al menos 8 caracteres") String password,
        @NotBlank(message = "Confirma la contraseña") String confirmacionPassword,
        @NotBlank(message = "Selecciona una talla") @Pattern(regexp = "S|M|L|XL|XXL", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Talla no válida") String talla,
        boolean aceptaSms,
        @AssertTrue(message = "Debes aceptar los términos y la política de privacidad") boolean aceptaTerminos) {

    /** Validación cruzada que no puede expresarse con una anotación de componente. */
    @JsonIgnore
    @AssertTrue(message = "Las contraseñas no coinciden")
    public boolean isPasswordCoincide() {
        return password != null && password.equals(confirmacionPassword);
    }
}
