package com.kult.tienda.service;

import com.kult.tienda.dto.CategoriaResumen;
import com.kult.tienda.dto.ProductoImagenResponse;
import com.kult.tienda.dto.ProductoResponse;
import com.kult.tienda.dto.VarianteResumen;
import com.kult.tienda.model.Categoria;
import com.kult.tienda.model.Producto;
import com.kult.tienda.model.Variante;
import com.kult.tienda.repository.CategoriaRepository;
import com.kult.tienda.repository.ProductoImagenRepository;
import com.kult.tienda.repository.ProductoRepository;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class ProductoService {
    private final ProductoRepository productos;
    private final CategoriaRepository categorias;
    private final ProductoImagenRepository imagenes;

    public ProductoService(
            ProductoRepository productos,
            CategoriaRepository categorias,
            ProductoImagenRepository imagenes) {
        this.productos = productos;
        this.categorias = categorias;
        this.imagenes = imagenes;
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> listar() {
        return productos.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProductoResponse obtener(Integer id) {
        return toResponse(obtenerEntidad(id));
    }

    public ProductoResponse crear(Producto producto) {
        validar(producto);
        producto.setIdProducto(null);
        producto.setCategoria(obtenerCategoria(producto));
        return toResponse(productos.save(producto));
    }

    public ProductoResponse actualizar(Integer id, Producto entrada) {
        validar(entrada);
        Producto actual = obtenerEntidad(id);
        actual.setNombre(entrada.getNombre().trim());
        actual.setDescripcion(entrada.getDescripcion());
        actual.setPrecio(entrada.getPrecio());
        actual.setGramajeGsm(entrada.getGramajeGsm());
        actual.setCategoria(obtenerCategoria(entrada));
        return toResponse(productos.save(actual));
    }

    public void eliminar(Integer id) {
        productos.delete(obtenerEntidad(id));
    }

    private Producto obtenerEntidad(Integer id) {
        return productos.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    }

    private Categoria obtenerCategoria(Producto producto) {
        if (producto.getCategoria() == null || producto.getCategoria().getIdCategoria() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La categoría es obligatoria");
        }
        return categorias.findById(producto.getCategoria().getIdCategoria()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "La categoría indicada no existe"));
    }

    private void validar(Producto producto) {
        if (producto == null || producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del producto es obligatorio");
        }
        if (producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El precio debe ser mayor o igual a cero");
        }
    }

    /** Convierte la entidad al contrato público consumido por el catálogo React. */
    private ProductoResponse toResponse(Producto producto) {
        List<Variante> variantes = producto.getVariantes() == null
                ? List.of()
                : producto.getVariantes();

        List<String> tallas = variantes.stream()
                .map(Variante::getTalla)
                .filter(talla -> talla != null && !talla.isBlank())
                .distinct()
                .sorted(Comparator.naturalOrder())
                .toList();

        List<String> colores = variantes.stream()
                .map(Variante::getColor)
                .filter(color -> color != null && !color.isBlank())
                .distinct()
                .sorted(Comparator.naturalOrder())
                .toList();

        int stock = variantes.stream()
                .map(Variante::getStock)
                .filter(Objects::nonNull)
                .reduce(0, Integer::sum);

        List<ProductoImagenResponse> imagenesProducto = producto.getIdProducto() == null
                ? List.of()
                : imagenes.findByProducto(producto.getIdProducto()).stream()
                        .map(ProductoImagenResponse::from)
                        .toList();

        Categoria categoria = producto.getCategoria();

        List<VarianteResumen> variantesResumen = variantes.stream()
                .map(v -> new VarianteResumen(v.getIdVariante(), v.getTalla(), v.getColor(), v.getStock()))
                .toList();

        return new ProductoResponse(
                producto.getIdProducto(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getGramajeGsm(),
                categoria == null ? null : new CategoriaResumen(categoria.getIdCategoria(), categoria.getNombre()),
                tallas,
                colores,
                stock,
                imagenesProducto,
                variantesResumen);
    }
}
