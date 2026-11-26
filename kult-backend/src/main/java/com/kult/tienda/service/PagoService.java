package com.kult.tienda.service;

import com.kult.tienda.model.Pago;
import com.kult.tienda.model.Pedido;
import com.kult.tienda.repository.PagoRepository;
import com.kult.tienda.repository.PedidoRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class PagoService {
    private final PagoRepository pagos;
    private final PedidoRepository pedidos;

    public PagoService(PagoRepository pagos, PedidoRepository pedidos) {
        this.pagos = pagos;
        this.pedidos = pedidos;
    }

    @Transactional(readOnly = true)
    public List<Pago> listar() {
        return pagos.findAll();
    }

    @Transactional(readOnly = true)
    public Pago obtener(Integer id) {
        return pagos.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Pago no encontrado"));
    }

    public Pago crear(Pago entrada) {
        validar(entrada);
        entrada.setIdPago(null);
        entrada.setPedido(buscarPedido(entrada));
        entrada.setFechaPago(entrada.getFechaPago() == null ? LocalDateTime.now() : entrada.getFechaPago());
        entrada.setEstado(entrada.getEstado() == null ? "APROBADO" : entrada.getEstado());
        return pagos.save(entrada);
    }

    public Pago actualizar(Integer id, Pago entrada) {
        validar(entrada);
        Pago actual = obtener(id);
        actual.setPedido(buscarPedido(entrada));
        actual.setMedio(entrada.getMedio().trim());
        actual.setValor(entrada.getValor());
        actual.setFechaPago(entrada.getFechaPago());
        actual.setEstado(entrada.getEstado());
        return pagos.save(actual);
    }

    public void eliminar(Integer id) {
        pagos.delete(obtener(id));
    }

    private Pedido buscarPedido(Pago pago) {
        if (pago.getPedido() == null || pago.getPedido().getIdPedido() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El pedido es obligatorio");
        }
        return pedidos.findById(pago.getPedido().getIdPedido()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "El pedido indicado no existe"));
    }

    private void validar(Pago pago) {
        if (pago == null || pago.getMedio() == null || pago.getMedio().isBlank()
                || pago.getValor() == null || pago.getValor().compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El medio de pago es obligatorio y el valor no puede ser negativo");
        }
    }
}
