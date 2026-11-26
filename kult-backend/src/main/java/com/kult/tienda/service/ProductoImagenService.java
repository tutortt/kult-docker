package com.kult.tienda.service;

import com.kult.tienda.dto.ProductoImagenResponse;
import com.kult.tienda.model.Producto;
import com.kult.tienda.model.ProductoImagen;
import com.kult.tienda.repository.ProductoImagenRepository;
import com.kult.tienda.repository.ProductoRepository;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

/** Gestión de las imágenes BLOB del catálogo. */
@Service
@Transactional
public class ProductoImagenService {

    private static final Set<String> MIME_PERMITIDOS =
            Set.of("image/webp", "image/png", "image/jpeg", "image/gif");
    private static final long MAX_BYTES = 5L * 1024 * 1024;

    private final ProductoImagenRepository imagenes;
    private final ProductoRepository productos;

    /** Serializa las cargas para evitar deadlocks al desmarcar la imagen principal. */
    private final Object uploadLock = new Object();

    public ProductoImagenService(ProductoImagenRepository imagenes, ProductoRepository productos) {
        this.imagenes = imagenes;
        this.productos = productos;
    }

    @Transactional(readOnly = true)
    public List<ProductoImagenResponse> listar(Integer idProducto) {
        return imagenes.findByProducto(idProducto).stream()
                .map(ProductoImagenResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductoImagen obtener(Integer idImagen) {
        return imagenes.findById(idImagen).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Imagen no encontrada"));
    }

    public ProductoImagenResponse subir(
            Integer idProducto, MultipartFile archivo, boolean principal, Integer orden) {
        synchronized (uploadLock) {
            Producto producto = productos.findById(idProducto).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

            if (archivo == null || archivo.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Se requiere un archivo de imagen");
            }
            if (!MIME_PERMITIDOS.contains(archivo.getContentType())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Formato no permitido; usa WebP, PNG, JPEG o GIF");
            }
            if (archivo.getSize() > MAX_BYTES) {
                throw new ResponseStatusException(HttpStatus.CONTENT_TOO_LARGE, "La imagen supera los 5 MB");
            }

            if (principal) {
                imagenes.desmarcarPrincipales(idProducto);
            }

            ProductoImagen imagen = new ProductoImagen();
            imagen.setProducto(producto);
            imagen.setMimeType(archivo.getContentType());
            imagen.setNombreArchivo(archivo.getOriginalFilename() == null
                    ? "imagen-" + idProducto
                    : archivo.getOriginalFilename());
            imagen.setOrden(orden == null ? 0 : orden);
            imagen.setPrincipal(principal);

            try {
                imagen.setImagen(archivo.getBytes());
            } catch (IOException exception) {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "No fue posible leer el archivo", exception);
            }

            return ProductoImagenResponse.from(imagenes.save(imagen));
        }
    }

    public void eliminar(Integer idImagen) {
        imagenes.delete(obtener(idImagen));
    }
}
