package com.kult.tienda.service;

import com.kult.tienda.model.Producto;
import com.kult.tienda.model.Variante;
import com.kult.tienda.repository.ProductoRepository;
import com.kult.tienda.repository.VarianteRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class VarianteService {
    private final VarianteRepository variantes;
    private final ProductoRepository productos;

    public VarianteService(VarianteRepository variantes, ProductoRepository productos) {
        this.variantes = variantes;
        this.productos = productos;
    }

    @Transactional(readOnly = true)
    public List<Variante> listar() {
        return variantes.findAll();
    }

    @Transactional(readOnly = true)
    public Variante obtener(Integer id) {
        return variantes.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Variante no encontrada"));
    }

    public Variante crear(Variante entrada) {
        validar(entrada);
        entrada.setIdVariante(null);
        entrada.setProducto(obtenerProducto(entrada));
        return variantes.save(entrada);
    }

    public Variante actualizar(Integer id, Variante entrada) {
        validar(entrada);
        Variante actual = obtener(id);
        actual.setProducto(obtenerProducto(entrada));
        actual.setTalla(entrada.getTalla().trim());
        actual.setColor(entrada.getColor().trim());
        actual.setStock(entrada.getStock());
        return variantes.save(actual);
    }

    public void eliminar(Integer id) {
        variantes.delete(obtener(id));
    }

    private Producto obtenerProducto(Variante variante) {
        if (variante.getProducto() == null || variante.getProducto().getIdProducto() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El producto es obligatorio");
        }
        return productos.findById(variante.getProducto().getIdProducto()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "El producto indicado no existe"));
    }

    private void validar(Variante variante) {
        if (variante == null || variante.getTalla() == null || variante.getTalla().isBlank()
                || variante.getColor() == null || variante.getColor().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La talla y el color son obligatorios");
        }
        if (variante.getStock() == null || variante.getStock() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El stock no puede ser negativo");
        }
    }
}
