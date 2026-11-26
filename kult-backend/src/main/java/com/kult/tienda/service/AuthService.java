package com.kult.tienda.service;

import com.kult.tienda.dto.AdminLoginRequest;
import com.kult.tienda.dto.LoginRequest;
import com.kult.tienda.dto.LoginResponse;
import com.kult.tienda.dto.RegistrationRequest;
import com.kult.tienda.dto.RegistrationResponse;
import com.kult.tienda.model.Usuario;
import com.kult.tienda.repository.UsuarioRepository;
import java.util.Locale;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/** Casos de uso de registro y acceso de miembros KULT. */
@Service
@Transactional
public class AuthService {

    private static final Set<String> CIUDADES = Set.of(
            "Bogotá", "Medellín", "Cali", "Barranquilla", "Cartagena", "Bucaramanga", "Otra ciudad");
    private static final Set<String> TALLAS = Set.of("S", "M", "L", "XL", "XXL");

    private final UsuarioRepository usuarios;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarios, PasswordEncoder passwordEncoder) {
        this.usuarios = usuarios;
        this.passwordEncoder = passwordEncoder;
    }

    /** Crea un usuario con contraseña hasheada y datos de despacho. */
    public RegistrationResponse registrar(RegistrationRequest solicitud) {
        String correo = solicitud.correo().trim().toLowerCase(Locale.ROOT);
        String identificacion = normalizarIdentificacion(solicitud.identificacion());
        String ciudad = solicitud.ciudad().trim();
        String talla = solicitud.talla().trim().toUpperCase(Locale.ROOT);
        String celular = normalizarCelular(solicitud.celular());

        if (!identificacion.matches("[A-Z0-9]{6,20}")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La identificación debe tener entre 6 y 20 caracteres alfanuméricos");
        }
        if (!CIUDADES.contains(ciudad)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La ciudad de despacho no es válida");
        }
        if (!TALLAS.contains(talla)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La talla seleccionada no es válida");
        }
        if (!celular.matches("[1-3]\\d{9}")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El móvil debe tener 10 dígitos colombianos");
        }
        validarPassword(solicitud.password());

        if (usuarios.existsByCorreoIgnoreCase(correo)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado");
        }
        if (usuarios.existsByDocumentoIgnoreCase(identificacion)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "El número de identificación ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(solicitud.nombre().trim());
        usuario.setApellido(solicitud.apellido().trim());
        usuario.setDocumento(identificacion);
        usuario.setCorreo(correo);
        usuario.setCelular(celular);
        usuario.setCiudad(ciudad);
        usuario.setTalla(talla);
        usuario.setAceptaSms(solicitud.aceptaSms());
        usuario.setAceptaTerminos(solicitud.aceptaTerminos());
        usuario.setRol("CLIENTE");
        usuario.setPasswordHash(passwordEncoder.encode(solicitud.password()));

        Usuario guardado = usuarios.save(usuario);

        return new RegistrationResponse(
                guardado.getIdUsuario(),
                guardado.getNombre(),
                guardado.getDocumento(),
                guardado.getCorreo(),
                guardado.getCiudad(),
                guardado.getTalla(),
                "Identificador de miembro creado correctamente");
    }

    /** Valida credenciales contra el hash almacenado. */
    public LoginResponse login(LoginRequest solicitud) {
        Usuario usuario = validarCredenciales(solicitud.email(), solicitud.password());
        String rol = usuario.getRol() == null || usuario.getRol().isBlank() ? "CLIENTE" : usuario.getRol();
        return new LoginResponse(usuario.getIdUsuario(), usuario.getNombre(), usuario.getDocumento(), usuario.getCorreo(), rol,
                "Identificación aceptada. Acceso autorizado.");
    }

    /** Acceso administrativo. Si no existe un admin, el primero que acceda con credenciales válidas es promovido. */
    public LoginResponse loginAdmin(AdminLoginRequest solicitud) {
        Usuario usuario = validarCredenciales(solicitud.email(), solicitud.password());
        String rol = usuario.getRol() == null ? "" : usuario.getRol().trim().toUpperCase(Locale.ROOT);
        if (!"ADMIN".equals(rol)) {
            if (usuarios.countByRolIgnoreCase("ADMIN") == 0) {
                usuario.setRol("ADMIN");
                usuarios.save(usuario);
                rol = "ADMIN";
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos de administrador");
            }
        }
        return new LoginResponse(usuario.getIdUsuario(), usuario.getNombre(), usuario.getDocumento(), usuario.getCorreo(), rol,
                "Acceso de administrador autorizado.");
    }

    private Usuario validarCredenciales(String email, String password) {
        Usuario usuario = usuarios.findByCorreoIgnoreCase(email.trim()).orElseThrow(() -> credencialesInvalidas());
        String hash = usuario.getPasswordHash();
        if (hash == null || hash.isBlank() || !passwordEncoder.matches(password, hash)) throw credencialesInvalidas();
        return usuario;
    }

    private ResponseStatusException credencialesInvalidas() {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Correo o contraseña incorrectos");
    }

    /** Conserva letras y dígitos; omite espacios, puntos y guiones. */
    private String normalizarIdentificacion(String valor) {
        return valor.replaceAll("[^A-Za-z0-9]", "").toUpperCase(Locale.ROOT);
    }

    /** Acepta números sueltos o con el indicativo internacional +57. */
    private String normalizarCelular(String valor) {
        String digitos = valor.replaceAll("[^\\d]", "");
        if (digitos.length() == 12 && digitos.startsWith("57")) {
            return digitos.substring(2);
        }
        return digitos;
    }

    private void validarPassword(String password) {
        if (password.length() < 8
                || !password.chars().anyMatch(Character::isLetter)
                || !password.chars().anyMatch(Character::isDigit)
                || !password.chars().anyMatch(caracter -> !Character.isLetterOrDigit(caracter))) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La contraseña debe tener al menos 8 caracteres, letras, números y un carácter especial");
        }
    }
}
