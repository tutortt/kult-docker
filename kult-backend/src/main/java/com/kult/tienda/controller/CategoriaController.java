
package com.kult.tienda.controller;

import com.kult.tienda.model.Categoria;
import com.kult.tienda.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin (origins = "http://localhost:5173")
@RestController
@RequestMapping ("/api/categoria")
@Tag(name = "Categorías", description = "Categorías de productos")
public class CategoriaController {
    
    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService cs) {
        this.categoriaService = cs;
    }
    
    @GetMapping( "/")
    @Operation(summary = "Listar categorías")
    public List<Categoria> listar(){
    return categoriaService.ListarCategorias();
    }
    
   @GetMapping("/{id}")
   @Operation(summary = "Consultar categoría por id")
   public Categoria mostrarCategoria(@PathVariable Integer id){
   return categoriaService.unaCategoria(id);
   }
    
   @PostMapping("/")
   @ResponseStatus(HttpStatus.CREATED)
   @Operation(summary = "Crear categoría")
   public Categoria registrar(@RequestBody Categoria cat){
   return categoriaService.regitrarCategoria(cat);
   }
   
   @PutMapping("/{id}")
   @Operation(summary = "Actualizar categoría")
   public Categoria actualizar(@PathVariable Integer id,@RequestBody Categoria cat ){
   return categoriaService.modificarCategoria(id, cat);
   }
   
   @DeleteMapping("/{id}")
   @ResponseStatus(HttpStatus.NO_CONTENT)
   @Operation(summary = "Eliminar categoría", description = "No se puede eliminar si tiene productos asociados.")
   public void suprumir(@PathVariable Integer id){
   categoriaService.borrarCategoria(id);
   }
   
}
