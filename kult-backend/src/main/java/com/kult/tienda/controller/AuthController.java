package com.kult.tienda.controller;

import com.kult.tienda.dto.AdminLoginRequest;
import com.kult.tienda.dto.LoginRequest;
import com.kult.tienda.dto.LoginResponse;
import com.kult.tienda.dto.RegistrationRequest;
import com.kult.tienda.dto.RegistrationResponse;
import com.kult.tienda.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Registro y acceso de miembros KULT")
public class AuthController {
    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear cuenta de miembro")
    public RegistrationResponse registrar(@Valid @RequestBody RegistrationRequest solicitud) {
        return service.registrar(solicitud);
    }

    @PostMapping("/login")
    @Operation(summary = "Validar credenciales de acceso")
    public LoginResponse login(@Valid @RequestBody LoginRequest solicitud) {
        return service.login(solicitud);
    }

    @PostMapping("/admin/login")
    @Operation(summary = "Validar credenciales de administrador")
    public LoginResponse loginAdmin(@Valid @RequestBody AdminLoginRequest solicitud) {
        return service.loginAdmin(solicitud);
    }
}
