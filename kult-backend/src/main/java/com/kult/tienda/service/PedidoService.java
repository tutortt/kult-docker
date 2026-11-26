package com.kult.tienda.service;

import com.kult.tienda.model.Direccion;
import com.kult.tienda.model.EstadoPedido;
import com.kult.tienda.model.Pedido;
import com.kult.tienda.model.Usuario;
import com.kult.tienda.repository.DireccionRepository;
import com.kult.tienda.repository.PedidoRepository;
import com.kult.tienda.repository.UsuarioRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class PedidoService {
    private final PedidoRepository pedidos;
    private final UsuarioRepository usuarios;
    private final DireccionRepository direcciones;

    public PedidoService(PedidoRepository pedidos, UsuarioRepository usuarios,
            DireccionRepository direcciones) {
        this.pedidos = pedidos;
        this.usuarios = usuarios;
        this.direcciones = direcciones;
    }

    @Transactional(readOnly = true)
    public List<Pedido> listar() {
        return pedidos.findAll();
    }

    @Transactional(readOnly = true)
    public Pedido obtener(Integer id) {
        return pedidos.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));
    }

    public Pedido crear(Pedido entrada) {
        validar(entrada);
        entrada.setIdPedido(null);
        entrada.setFecha(entrada.getFecha() == null ? LocalDateTime.now() : entrada.getFecha());
        entrada.setEstado(entrada.getEstado() == null ? EstadoPedido.PENDIENTE : entrada.getEstado());
        entrada.setCostoEnvio(entrada.getCostoEnvio() == null ? new BigDecimal("12000.00") : entrada.getCostoEnvio());
        asignarRelaciones(entrada);
        return pedidos.save(entrada);
    }

    public Pedido actualizar(Integer id, Pedido entrada) {
        validar(entrada);
        Pedido actual = obtener(id);
        actual.setEstado(entrada.getEstado() == null ? actual.getEstado() : entrada.getEstado());
        actual.setCostoEnvio(entrada.getCostoEnvio());
        actual.setSubtotal(entrada.getSubtotal());
        actual.setTotal(entrada.getTotal());
        asignarRelaciones(entrada);
        actual.setUsuario(entrada.getUsuario());
        actual.setDireccion(entrada.getDireccion());
        return pedidos.save(actual);
    }

    public void eliminar(Integer id) {
        pedidos.delete(obtener(id));
    }

    private void asignarRelaciones(Pedido pedido) {
        Usuario usuario = buscarUsuario(pedido);
        Direccion direccion = buscarDireccion(pedido);
        if (!usuario.getIdUsuario().equals(direccion.getUsuario().getIdUsuario())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La dirección no pertenece al usuario del pedido");
        }
        pedido.setUsuario(usuario);
        pedido.setDireccion(direccion);
    }

    private Usuario buscarUsuario(Pedido pedido) {
        if (pedido.getUsuario() == null || pedido.getUsuario().getIdUsuario() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario es obligatorio");
        }
        return usuarios.findById(pedido.getUsuario().getIdUsuario()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario indicado no existe"));
    }

    private Direccion buscarDireccion(Pedido pedido) {
        if (pedido.getDireccion() == null || pedido.getDireccion().getIdDireccion() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La dirección es obligatoria");
        }
        return direcciones.findById(pedido.getDireccion().getIdDireccion()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "La dirección indicada no existe"));
    }

    private void validar(Pedido pedido) {
        if (pedido == null || pedido.getSubtotal() == null || pedido.getTotal() == null
                || pedido.getSubtotal().compareTo(BigDecimal.ZERO) < 0
                || pedido.getTotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El subtotal y el total son obligatorios y no pueden ser negativos");
        }
    }
}
