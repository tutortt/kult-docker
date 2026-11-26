package com.kult.tienda.controller;

import com.kult.tienda.dto.ProductoResponse;
import com.kult.tienda.model.Producto;
import com.kult.tienda.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/producto")
@Tag(name = "Productos", description = "Productos y su categoría asociada")
public class ProductoController {
    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping("/")
    @Operation(summary = "Listar productos")
    public List<ProductoResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar producto por id")
    public ProductoResponse obtener(@PathVariable Integer id) {
        return service.obtener(id);
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear producto", description = "La categoría relacionada se identifica con categoria.idCategoria.")
    public ProductoResponse crear(@RequestBody Producto producto) {
        return service.crear(producto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto")
    public ProductoResponse actualizar(@PathVariable Integer id, @RequestBody Producto producto) {
        return service.actualizar(id, producto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar producto", description = "La eliminación está sujeta a las restricciones de la base de datos.")
    public void eliminar(@PathVariable Integer id) {
        service.eliminar(id);
    }
}
