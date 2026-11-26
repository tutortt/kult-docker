package com.kult.tienda.controller;

import com.kult.tienda.model.Pago;
import com.kult.tienda.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/pago")
@Tag(name = "Pagos", description = "Pagos asociados de forma única a un pedido")
public class PagoController {
    private final PagoService service;

    public PagoController(PagoService service) {
        this.service = service;
    }

    @GetMapping( "/")
    @Operation(summary = "Listar pagos")
    public List<Pago> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar pago por id")
    public Pago obtener(@PathVariable Integer id) {
        return service.obtener(id);
    }

    @PostMapping( "/")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar pago", description = "El pedido relacionado se identifica con pedido.idPedido.")
    public Pago crear(@RequestBody Pago pago) {
        return service.crear(pago);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar pago")
    public Pago actualizar(@PathVariable Integer id, @RequestBody Pago pago) {
        return service.actualizar(id, pago);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar pago")
    public void eliminar(@PathVariable Integer id) {
        service.eliminar(id);
    }
}
