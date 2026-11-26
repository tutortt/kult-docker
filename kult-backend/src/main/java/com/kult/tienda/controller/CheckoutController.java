package com.kult.tienda.controller;

import com.kult.tienda.dto.CheckoutRequest;
import com.kult.tienda.dto.CheckoutResponse;
import com.kult.tienda.service.CheckoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/checkout")
@Tag(name = "Checkout", description = "Compra simulada con validación y actualización de inventario")
public class CheckoutController {
    private final CheckoutService service;

    public CheckoutController(CheckoutService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Confirmar compra simulada")
    public CheckoutResponse checkout(@Valid @RequestBody CheckoutRequest request) {
        return service.checkout(request);
    }
}
