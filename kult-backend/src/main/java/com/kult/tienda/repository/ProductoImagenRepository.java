package com.kult.tienda.repository;

import com.kult.tienda.model.ProductoImagen;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductoImagenRepository extends JpaRepository<ProductoImagen, Integer> {

    @Query("""
            select i from ProductoImagen i
            where i.producto.idProducto = :idProducto
            order by i.principal desc, i.orden asc, i.idImagen asc
            """)
    List<ProductoImagen> findByProducto(@Param("idProducto") Integer idProducto);

    @Modifying
    @Query("update ProductoImagen i set i.principal = false where i.producto.idProducto = :idProducto")
    void desmarcarPrincipales(@Param("idProducto") Integer idProducto);
}
