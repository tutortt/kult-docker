package com.kult.tienda.service;

import com.kult.tienda.model.Direccion;
import com.kult.tienda.model.Usuario;
import com.kult.tienda.repository.DireccionRepository;
import com.kult.tienda.repository.UsuarioRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class DireccionService {
    private final DireccionRepository direcciones;
    private final UsuarioRepository usuarios;

    public DireccionService(DireccionRepository direcciones, UsuarioRepository usuarios) {
        this.direcciones = direcciones;
        this.usuarios = usuarios;
    }

    @Transactional(readOnly = true)
    public List<Direccion> listar() {
        return direcciones.findAll();
    }

    @Transactional(readOnly = true)
    public Direccion obtener(Integer id) {
        return direcciones.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Dirección no encontrada"));
    }

    public Direccion crear(Direccion entrada) {
        validar(entrada);
        entrada.setIdDireccion(null);
        entrada.setUsuario(obtenerUsuario(entrada));
        return direcciones.save(entrada);
    }

    public Direccion actualizar(Integer id, Direccion entrada) {
        validar(entrada);
        Direccion actual = obtener(id);
        actual.setUsuario(obtenerUsuario(entrada));
        actual.setDireccion(entrada.getDireccion().trim());
        actual.setCiudad(entrada.getCiudad().trim());
        actual.setDepartamento(entrada.getDepartamento().trim());
        actual.setCodigoPostal(entrada.getCodigoPostal());
        return direcciones.save(actual);
    }

    public void eliminar(Integer id) {
        direcciones.delete(obtener(id));
    }

    private Usuario obtenerUsuario(Direccion direccion) {
        if (direccion.getUsuario() == null || direccion.getUsuario().getIdUsuario() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario es obligatorio");
        }
        return usuarios.findById(direccion.getUsuario().getIdUsuario()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario indicado no existe"));
    }

    private void validar(Direccion direccion) {
        if (direccion == null || vacio(direccion.getDireccion()) || vacio(direccion.getCiudad())
                || vacio(direccion.getDepartamento())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Dirección, ciudad y departamento son obligatorios");
        }
    }

    private boolean vacio(String valor) {
        return valor == null || valor.isBlank();
    }
}
