package com.kult.tienda.service;

import com.kult.tienda.model.DetallePedido;
import com.kult.tienda.model.DetallePedidoId;
import com.kult.tienda.model.Pedido;
import com.kult.tienda.model.Variante;
import com.kult.tienda.repository.DetallePedidoRepository;
import com.kult.tienda.repository.PedidoRepository;
import com.kult.tienda.repository.VarianteRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class DetallePedidoService {
    private final DetallePedidoRepository detalles;
    private final PedidoRepository pedidos;
    private final VarianteRepository variantes;

    public DetallePedidoService(DetallePedidoRepository detalles, PedidoRepository pedidos,
            VarianteRepository variantes) {
        this.detalles = detalles;
        this.pedidos = pedidos;
        this.variantes = variantes;
    }

    @Transactional(readOnly = true)
    public List<DetallePedido> listar() {
        return detalles.findAll();
    }

    @Transactional(readOnly = true)
    public DetallePedido obtener(Integer idPedido, Integer idVariante) {
        DetallePedidoId id = new DetallePedidoId();
        id.setIdPedido(idPedido);
        id.setIdVariante(idVariante);
        return detalles.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Detalle de pedido no encontrado"));
    }

    public DetallePedido crear(DetallePedido entrada) {
        validar(entrada);
        if (entrada.getId() == null || entrada.getId().getIdPedido() == null
                || entrada.getId().getIdVariante() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Los identificadores del pedido y de la variante son obligatorios");
        }
        DetallePedidoId id = entrada.getId();
        entrada.setPedido(buscarPedido(id.getIdPedido()));
        entrada.setVariante(buscarVariante(id.getIdVariante()));
        return detalles.save(entrada);
    }

    public DetallePedido actualizar(Integer idPedido, Integer idVariante, DetallePedido entrada) {
        validar(entrada);
        DetallePedido actual = obtener(idPedido, idVariante);
        actual.setCantidad(entrada.getCantidad());
        actual.setPrecioUnitario(entrada.getPrecioUnitario());
        return detalles.save(actual);
    }

    public void eliminar(Integer idPedido, Integer idVariante) {
        detalles.delete(obtener(idPedido, idVariante));
    }

    private Pedido buscarPedido(Integer id) {
        return pedidos.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "El pedido indicado no existe"));
    }

    private Variante buscarVariante(Integer id) {
        return variantes.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "La variante indicada no existe"));
    }

    private void validar(DetallePedido detalle) {
        if (detalle == null || detalle.getCantidad() == null || detalle.getCantidad() < 1
                || detalle.getPrecioUnitario() == null
                || detalle.getPrecioUnitario().compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La cantidad debe ser positiva y el precio unitario no puede ser negativo");
        }
    }
}
