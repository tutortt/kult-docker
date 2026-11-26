package com.kult.tienda.controller;

import com.kult.tienda.model.DetallePedido;
import com.kult.tienda.service.DetallePedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/detalle-pedido")
@Tag(name = "Detalles de pedido", description = "Variantes, cantidades y precios guardados en cada pedido")
public class DetallePedidoController {
    private final DetallePedidoService service;

    public DetallePedidoController(DetallePedidoService service) {
        this.service = service;
    }

    @GetMapping("/")
    @Operation(summary = "Listar detalles de pedido")
    public List<DetallePedido> listar() {
        return service.listar();
    }

    @GetMapping("/{idPedido}/{idVariante}")
    @Operation(summary = "Consultar detalle por clave compuesta")
    public DetallePedido obtener(@PathVariable Integer idPedido, @PathVariable Integer idVariante) {
        return service.obtener(idPedido, idVariante);
    }

    @PostMapping( "/")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Agregar detalle", description = "El JSON debe incluir id.idPedido e id.idVariante, además de cantidad y precioUnitario.")
    public DetallePedido crear(@RequestBody DetallePedido detalle) {
        return service.crear(detalle);
    }

    @PutMapping("/{idPedido}/{idVariante}")
    @Operation(summary = "Actualizar detalle", description = "La clave compuesta se toma de la ruta; solo se modifican cantidad y precio unitario.")
    public DetallePedido actualizar(@PathVariable Integer idPedido, @PathVariable Integer idVariante,
            @RequestBody DetallePedido detalle) {
        return service.actualizar(idPedido, idVariante, detalle);
    }

    @DeleteMapping("/{idPedido}/{idVariante}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar detalle por clave compuesta")
    public void eliminar(@PathVariable Integer idPedido, @PathVariable Integer idVariante) {
        service.eliminar(idPedido, idVariante);
    }
}
