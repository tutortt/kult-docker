package com.kult.tienda.service;

import com.kult.tienda.dto.CheckoutItemRequest;
import com.kult.tienda.dto.CheckoutItemResponse;
import com.kult.tienda.dto.CheckoutRequest;
import com.kult.tienda.dto.CheckoutResponse;
import com.kult.tienda.model.DetallePedido;
import com.kult.tienda.model.DetallePedidoId;
import com.kult.tienda.model.Direccion;
import com.kult.tienda.model.EstadoPedido;
import com.kult.tienda.model.Pago;
import com.kult.tienda.model.Pedido;
import com.kult.tienda.model.Usuario;
import com.kult.tienda.model.Variante;
import com.kult.tienda.repository.DetallePedidoRepository;
import com.kult.tienda.repository.DireccionRepository;
import com.kult.tienda.repository.PagoRepository;
import com.kult.tienda.repository.PedidoRepository;
import com.kult.tienda.repository.ProductoImagenRepository;
import com.kult.tienda.repository.UsuarioRepository;
import com.kult.tienda.repository.VarianteRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class CheckoutService {
    private static final BigDecimal COSTO_ENVIO = BigDecimal.ZERO;

    private final PedidoRepository pedidos;
    private final DetallePedidoRepository detalles;
    private final VarianteRepository variantes;
    private final UsuarioRepository usuarios;
    private final DireccionRepository direcciones;
    private final PagoRepository pagos;
    private final ProductoImagenRepository imagenesProducto;

    public CheckoutService(PedidoRepository pedidos, DetallePedidoRepository detalles,
            VarianteRepository variantes, UsuarioRepository usuarios,
            DireccionRepository direcciones, PagoRepository pagos,
            ProductoImagenRepository imagenesProducto) {
        this.pedidos = pedidos;
        this.detalles = detalles;
        this.variantes = variantes;
        this.usuarios = usuarios;
        this.direcciones = direcciones;
        this.pagos = pagos;
        this.imagenesProducto = imagenesProducto;
    }

    public CheckoutResponse checkout(CheckoutRequest request) {
        validar(request);
        Map<Integer, Integer> cantidades = consolidarItems(request.items());
        List<Variante> variantesPedido = cargarVariantes(cantidades);

        Usuario usuario = resolverUsuario(request);
        Direccion direccion = crearDireccion(usuario, request);

        Pedido pedido = new Pedido();
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.PAGADO);
        pedido.setCostoEnvio(COSTO_ENVIO);
        pedido.setSubtotal(BigDecimal.ZERO);
        pedido.setTotal(BigDecimal.ZERO);
        pedido.setUsuario(usuario);
        pedido.setDireccion(direccion);
        pedido = pedidos.save(pedido);

        BigDecimal subtotal = BigDecimal.ZERO;
        int cantidadArticulos = 0;
        List<CheckoutItemResponse> items = new ArrayList<>();

        for (Variante variante : variantesPedido) {
            int cantidad = cantidades.get(variante.getIdVariante());
            int stock = variante.getStock() == null ? 0 : variante.getStock();
            if (cantidad > stock) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Stock insuficiente para " + nombreProducto(variante)
                                + " (" + variante.getTalla() + "/" + variante.getColor() + "). Disponible: " + stock);
            }

            variante.setStock(stock - cantidad);
            variantes.save(variante);

            BigDecimal precioUnitario = variante.getProducto().getPrecio();
            BigDecimal linea = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
            subtotal = subtotal.add(linea);
            cantidadArticulos += cantidad;

            DetallePedido detalle = new DetallePedido();
            DetallePedidoId detalleId = new DetallePedidoId();
            detalleId.setIdPedido(pedido.getIdPedido());
            detalleId.setIdVariante(variante.getIdVariante());
            detalle.setId(detalleId);
            detalle.setPedido(pedido);
            detalle.setVariante(variante);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(precioUnitario);
            detalles.save(detalle);

            String imagenUrl = imagenesProducto.findByProducto(variante.getProducto().getIdProducto()).stream()
                    .map(img -> "/api/producto/imagenes/" + img.getIdImagen() + "/contenido")
                    .findFirst().orElse(null);

            items.add(new CheckoutItemResponse(
                    variante.getProducto().getIdProducto(),
                    variante.getIdVariante(),
                    variante.getProducto().getNombre(),
                    variante.getTalla(),
                    variante.getColor(),
                    imagenUrl,
                    cantidad,
                    precioUnitario,
                    linea));
        }

        pedido.setSubtotal(subtotal);
        pedido.setTotal(subtotal.add(COSTO_ENVIO));
        pedidos.save(pedido);

        Pago pago = new Pago();
        pago.setPedido(pedido);
        pago.setMedio(request.medioPago().trim());
        pago.setValor(pedido.getTotal());
        pago.setFechaPago(LocalDateTime.now());
        pago.setEstado("APROBADO");
        pagos.save(pago);

        return new CheckoutResponse(
                pedido.getIdPedido(),
                pedido.getEstado().name(),
                pago.getEstado(),
                pago.getMedio(),
                pedido.getFecha(),
                pedido.getSubtotal(),
                pedido.getCostoEnvio(),
                pedido.getTotal(),
                cantidadArticulos,
                items,
                "Compra simulada confirmada. El inventario fue actualizado.");
    }

    private void validar(CheckoutRequest request) {
        if (request == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solicitud inválida");
        if (request.items() == null || request.items().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El carrito no puede estar vacío");
        }
    }

    private Map<Integer, Integer> consolidarItems(List<CheckoutItemRequest> items) {
        Map<Integer, Integer> cantidades = new LinkedHashMap<>();
        for (CheckoutItemRequest item : items) {
            if (item == null || item.idVariante() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cada ítem debe incluir una variante");
            }
            int cantidad = item.cantidad() == null ? 0 : item.cantidad();
            if (cantidad < 1) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad debe ser mayor a 0");
            }
            cantidades.merge(item.idVariante(), cantidad, Integer::sum);
        }
        return cantidades;
    }

    private List<Variante> cargarVariantes(Map<Integer, Integer> cantidades) {
        List<Integer> ids = new ArrayList<>(cantidades.keySet());
        List<Variante> encontradas = variantes.findAllByIdForUpdate(ids);
        if (encontradas.size() != cantidades.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Una o más variantes del carrito no existen");
        }
        Map<Integer, Variante> byId = new LinkedHashMap<>();
        for (Variante v : encontradas) byId.put(v.getIdVariante(), v);
        List<Variante> ordenadas = new ArrayList<>();
        for (Integer id : ids) ordenadas.add(byId.get(id));
        return ordenadas;
    }

        private Usuario resolverUsuario(CheckoutRequest request) {
        String correo = request.correo().trim();
        return usuarios.findByCorreoIgnoreCase(correo).orElseGet(() -> {
            Usuario u = new Usuario();
            String nombre = request.nombre().trim();
            String[] partes = nombre.split("\s+", 2);
            u.setNombre(partes[0]);
            u.setApellido(partes.length > 1 ? partes[1] : "Cliente");
            u.setCorreo(correo);
            u.setPasswordHash("");
            u.setAceptaTerminos(true);
            return usuarios.save(u);
        });
    }

    private Direccion crearDireccion(Usuario usuario, CheckoutRequest request) {
        Direccion d = new Direccion();
        d.setUsuario(usuario);
        d.setDireccion(request.direccion().trim());
        d.setCiudad(request.ciudad().trim());
        d.setDepartamento(request.departamento().trim());
        d.setCodigoPostal(request.codigoPostal() == null || request.codigoPostal().isBlank() ? null : request.codigoPostal().trim());
        return direcciones.save(d);
    }

    private String nombreProducto(Variante variante) {
        return variante.getProducto() == null ? "Producto" : variante.getProducto().getNombre();
    }
}
