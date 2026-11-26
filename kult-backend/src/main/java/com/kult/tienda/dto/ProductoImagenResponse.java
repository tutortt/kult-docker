package com.kult.tienda.dto;

import com.kult.tienda.model.ProductoImagen;

/** Metadata pública de una imagen; el binario se sirve en una ruta aparte. */
public record ProductoImagenResponse(
        Integer idImagen,
        String url,
        String nombreArchivo,
        boolean principal,
        Integer orden) {

    public static ProductoImagenResponse from(ProductoImagen imagen) {
        return new ProductoImagenResponse(
                imagen.getIdImagen(),
                "/api/producto/imagenes/" + imagen.getIdImagen() + "/contenido",
                imagen.getNombreArchivo(),
                imagen.isPrincipal(),
                imagen.getOrden());
    }
}
