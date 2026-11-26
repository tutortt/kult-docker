package com.kult.tienda.repository;

import com.kult.tienda.model.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    boolean existsByCorreoIgnoreCase(String correo);

    boolean existsByDocumentoIgnoreCase(String documento);

    Optional<Usuario> findByCorreoIgnoreCase(String correo);

    long countByRolIgnoreCase(String rol);
}
