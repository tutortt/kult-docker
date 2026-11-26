package com.kult.tienda.controller;

import com.kult.tienda.model.Direccion;
import com.kult.tienda.service.DireccionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/direccion")
@Tag(name = "Direcciones", description = "Direcciones de envío asociadas a usuarios")
public class DireccionController {
    private final DireccionService service;

    public DireccionController(DireccionService service) {
        this.service = service;
    }

    @GetMapping( "/")
    @Operation(summary = "Listar direcciones")
    public List<Direccion> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar dirección por id")
    public Direccion obtener(@PathVariable Integer id) {
        return service.obtener(id);
    }

    @PostMapping( "/")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear dirección", description = "El usuario relacionado se identifica con usuario.idUsuario.")
    public Direccion crear(@RequestBody Direccion direccion) {
        return service.crear(direccion);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar dirección")
    public Direccion actualizar(@PathVariable Integer id, @RequestBody Direccion direccion) {
        return service.actualizar(id, direccion);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar dirección", description = "No se puede eliminar si la dirección está referenciada por un pedido.")
    public void eliminar(@PathVariable Integer id) {
        service.eliminar(id);
    }
}
