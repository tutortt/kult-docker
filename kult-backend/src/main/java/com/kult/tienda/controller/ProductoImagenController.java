package com.kult.tienda.controller;

import com.kult.tienda.dto.ProductoImagenResponse;
import com.kult.tienda.model.ProductoImagen;
import com.kult.tienda.service.ProductoImagenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/producto")
@Tag(name = "Imágenes de productos", description = "Carga y servir imágenes BLOB del catálogo")
public class ProductoImagenController {
    private final ProductoImagenService service;

    public ProductoImagenController(ProductoImagenService service) {
        this.service = service;
    }

    @GetMapping("/imagenes/{id}/contenido")
    @Operation(summary = "Obtener bytes de una imagen")
    public ResponseEntity<byte[]> contenido(@PathVariable Integer id) {
        ProductoImagen imagen = service.obtener(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(imagen.getMimeType()))
                .cacheControl(CacheControl.noCache())
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + imagen.getNombreArchivo() + "\"")
                .body(imagen.getImagen());
    }

    @GetMapping("/{id}/imagenes")
    @Operation(summary = "Listar imágenes de un producto")
    public List<ProductoImagenResponse> listar(@PathVariable Integer id) {
        return service.listar(id);
    }

    @PostMapping(value = "/{id}/imagenes", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Subir una imagen a un producto")
    public ProductoImagenResponse subir(
            @PathVariable Integer id,
            @RequestPart("file") org.springframework.web.multipart.MultipartFile file,
            @RequestParam(defaultValue = "false") boolean principal,
            @RequestParam(required = false) Integer orden) {
        return service.subir(id, file, principal, orden);
    }

    @DeleteMapping("/imagenes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar una imagen")
    public void eliminar(@PathVariable Integer id) {
        service.eliminar(id);
    }
}
