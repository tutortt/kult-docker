package com.kult.tienda.service;

import com.kult.tienda.model.Usuario;
import com.kult.tienda.repository.UsuarioRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class UsuarioService {
    private final UsuarioRepository usuarios;

    public UsuarioService(UsuarioRepository usuarios) {
        this.usuarios = usuarios;
    }

    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return usuarios.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario obtener(Integer id) {
        return usuarios.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    public Usuario crear(Usuario usuario) {
        validar(usuario);
        usuario.setIdUsuario(null);
        return usuarios.save(usuario);
    }

    public Usuario actualizar(Integer id, Usuario entrada) {
        validar(entrada);
        Usuario actual = obtener(id);
        actual.setNombre(entrada.getNombre().trim());
        actual.setApellido(entrada.getApellido().trim());
        actual.setCorreo(entrada.getCorreo().trim());
        actual.setCelular(entrada.getCelular());
        actual.setDocumento(entrada.getDocumento());
        return usuarios.save(actual);
    }

    public void eliminar(Integer id) {
        usuarios.delete(obtener(id));
    }

    private void validar(Usuario usuario) {
        if (usuario == null || esVacio(usuario.getNombre()) || esVacio(usuario.getApellido())
                || esVacio(usuario.getCorreo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Nombre, apellido y correo del usuario son obligatorios");
        }
    }

    private boolean esVacio(String valor) {
        return valor == null || valor.isBlank();
    }
}
