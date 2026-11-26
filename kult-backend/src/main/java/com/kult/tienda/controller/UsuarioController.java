package com.kult.tienda.controller;

import com.kult.tienda.model.Usuario;
import com.kult.tienda.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/usuario")
@Tag(name = "Usuarios", description = "Clientes registrados en la tienda")
public class UsuarioController {
    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping( "/")
    @Operation(summary = "Listar usuarios")
    public List<Usuario> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar usuario por id")
    public Usuario obtener(@PathVariable Integer id) {
        return service.obtener(id);
    }

    @PostMapping( "/")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar usuario")
    public Usuario crear(@RequestBody Usuario usuario) {
        return service.crear(usuario);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario")
    public Usuario actualizar(@PathVariable Integer id, @RequestBody Usuario usuario) {
        return service.actualizar(id, usuario);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar usuario", description = "La eliminación depende de las relaciones y restricciones de la base de datos.")
    public void eliminar(@PathVariable Integer id) {
        service.eliminar(id);
    }
}
