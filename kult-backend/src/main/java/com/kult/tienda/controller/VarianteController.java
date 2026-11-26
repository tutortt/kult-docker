package com.kult.tienda.controller;

import com.kult.tienda.model.Variante;
import com.kult.tienda.service.VarianteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/variante")
@Tag(name = "Variantes", description = "Tallas, colores e inventario de los productos")
public class VarianteController {
    private final VarianteService service;

    public VarianteController(VarianteService service) {
        this.service = service;
    }

    @GetMapping( "/")
    @Operation(summary = "Listar variantes")
    public List<Variante> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar variante por id")
    public Variante obtener(@PathVariable Integer id) {
        return service.obtener(id);
    }

    @PostMapping( "/")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear variante", description = "El producto relacionado se identifica con producto.idProducto.")
    public Variante crear(@RequestBody Variante variante) {
        return service.crear(variante);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar variante", description = "Actualiza sus datos y puede cambiar el producto asociado.")
    public Variante actualizar(@PathVariable Integer id, @RequestBody Variante variante) {
        return service.actualizar(id, variante);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar variante", description = "No se puede eliminar si está referenciada por detalles de pedido.")
    public void eliminar(@PathVariable Integer id) {
        service.eliminar(id);
    }
}
