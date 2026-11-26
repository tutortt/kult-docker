package com.kult.tienda.controller;

import com.kult.tienda.model.Pedido;
import com.kult.tienda.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/pedido")
@Tag(name = "Pedidos", description = "Órdenes de compra y sus datos de envío")
public class PedidoController {
    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @GetMapping("/")
    @Operation(summary = "Listar pedidos")
    public List<Pedido> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar pedido por id")
    public Pedido obtener(@PathVariable Integer id) {
        return service.obtener(id);
    }

    @PostMapping( "/")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear pedido", description = "Debe incluir usuario.idUsuario y direccion.idDireccion; la dirección debe pertenecer al usuario.")
    public Pedido crear(@RequestBody Pedido pedido) {
        return service.crear(pedido);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar pedido")
    public Pedido actualizar(@PathVariable Integer id, @RequestBody Pedido pedido) {
        return service.actualizar(id, pedido);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar pedido", description = "La eliminación también se rige por las relaciones de sus detalles y pagos.")
    public void eliminar(@PathVariable Integer id) {
        service.eliminar(id);
    }
}
