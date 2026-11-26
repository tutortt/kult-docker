package com.kult.tienda.dto;

import java.math.BigDecimal;
import java.util.List;

/** Producto listo para el catálogo, sin exponer entidades JPA ni colecciones perezosas. */
public record ProductoResponse(
        Integer idProducto,
        String nombre,
        String descripcion,
        BigDecimal precio,
        Integer gramajeGsm,
        CategoriaResumen categoria,
        List<String> tallas,
        List<String> colores,
        Integer stock,
        List<ProductoImagenResponse> imagenes,
        List<VarianteResumen> variantes) {
}
