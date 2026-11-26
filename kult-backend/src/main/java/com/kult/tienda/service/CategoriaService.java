package com.kult.tienda.service;

import com.kult.tienda.model.Categoria;
import com.kult.tienda.repository.CategoriaRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CategoriaService {
    
    private final CategoriaRepository categoriaRepository;
    
    public CategoriaService(CategoriaRepository c) {
        this.categoriaRepository = c;
    }
    
    public List<Categoria> ListarCategorias() {
        return categoriaRepository.findAll();
    }
    
    public Categoria unaCategoria(Integer id) {
        return categoriaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "no se encontro esa catagoria"));
    }
    
    public Categoria regitrarCategoria(Categoria cat) {
        validarCategoria(cat);
        return categoriaRepository.save(cat);
    }

    public void borrarCategoria(Integer id){
        categoriaRepository.delete(unaCategoria(id));
    }
    
    public Categoria modificarCategoria(Integer id, Categoria cat){
        validarCategoria(cat);
        Categoria actualOExistente = unaCategoria(id);
        actualOExistente.setNombre(cat.getNombre());
        actualOExistente.setDescripcion(cat.getDescripcion());
        return categoriaRepository.save(actualOExistente);
    } 
     
    
    private void validarCategoria(Categoria cat) {
        if (cat == null || cat.getNombre() == null || cat.getNombre().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nombre de la categoria es obligatorio");
        }
        cat.setNombre(cat.getNombre().trim());
    }
}
