package com.kult.tienda.controller;

import com.kult.tienda.model.Sede;
import com.kult.tienda.service.SedeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/sede")
@Tag(name = "Sedes", description = "Sedes e inventario multi-punto")
public class SedeController {
    private final SedeService service;
    public SedeController(SedeService service) { this.service = service; }

    @GetMapping("/")
    @Operation(summary = "Listar sedes")
    public List<Sede> listar() { return service.listar(); }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar sede")
    public Sede obtener(@PathVariable Integer id) { return service.obtener(id); }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear sede")
    public Sede crear(@RequestBody Sede sede) { return service.crear(sede); }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar sede")
    public Sede actualizar(@PathVariable Integer id, @RequestBody Sede sede) { return service.actualizar(id, sede); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar sede")
    public void eliminar(@PathVariable Integer id) { service.eliminar(id); }
}
