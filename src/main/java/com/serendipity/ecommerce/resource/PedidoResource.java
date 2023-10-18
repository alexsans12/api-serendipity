package com.serendipity.ecommerce.resource;

import com.serendipity.ecommerce.domain.*;
import com.serendipity.ecommerce.dto.UsuarioDTO;
import com.serendipity.ecommerce.dtomapper.PedidoDTOMapper;
import com.serendipity.ecommerce.dtomapper.ProductoDTOMapper;
import com.serendipity.ecommerce.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.serendipity.ecommerce.dtomapper.PedidoDTOMapper.fromPedido;
import static com.serendipity.ecommerce.dtomapper.UsuarioDTOMapper.toUsuario;
import static com.serendipity.ecommerce.enumeration.EstadoPedido.EN_PROCESO;
import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/pedido")
@RequiredArgsConstructor
public class PedidoResource {
    private final PedidoService pedidoService;
    private final PedidoProductoService pedidoProductoService;
    private final PagoService pagoService;
    private final DireccionService direccionService;
    private final CarritoService carritoService;

    @GetMapping("/list")
    public ResponseEntity<HttpResponse> getPedidos(@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("pedidos", pedidoService.getAllPedidos(page.orElse(0), size.orElse(10)).map(PedidoDTOMapper::fromPedido)))
                        .message("Lista de pedidos obtenida correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/get")
    public ResponseEntity<HttpResponse> getPedidosByUsuario(@AuthenticationPrincipal UsuarioDTO usuario, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("pedidos", pedidoService.getPedidosByUsuario(usuario.getIdUsuario(), page.orElse(0), size.orElse(10)).map(PedidoDTOMapper::fromPedido)))
                        .message("Lista de pedidos obtenida correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<HttpResponse> getPedidoById(@PathVariable Long id) {
        Pedido pedido = pedidoService.getPedidoById(id).orElse(null);
        if (pedido != null) {
            return ResponseEntity.ok(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .data(of("pedido", fromPedido(pedido)))
                            .message("Pedido obtenido correctamente")
                            .httpStatus(OK)
                            .httpStatusCode(OK.value())
                            .build()
            );
        } else {
            return ResponseEntity.badRequest().body(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .message("Pedido no encontrado")
                            .httpStatus(OK)
                            .httpStatusCode(OK.value())
                            .build()
            );
        }
    }

    @PostMapping("/create")
    public ResponseEntity<HttpResponse> createPedido(@AuthenticationPrincipal UsuarioDTO usuario, @RequestBody Pedido pedido) {
        Carrito carrito = carritoService.getCarritoByUsuario(usuario.getIdUsuario()).orElse(null);

        if (carrito != null && !carrito.getCarritoProductos().isEmpty()) {
            pedido.setIdUsuario(usuario.getIdUsuario());
            pedido.setUsuario(toUsuario(usuario));
            pedido.setFechaCreacion(now());
            pedido.setDireccion(direccionService.getDireccionById(pedido.getIdDireccion()).get());
            pedido.setTotal(carrito.getTotal());
            Pago pago = pagoService.getPagoById(pedido.getIdPago()).get();
            pedido.setPago(pago);
            pedido.setEstado(pedido.getEstado());
            Pedido pedidoCreado = pedidoService.createPedido(pedido);
            List<PedidoProducto> pedidoProductos = new ArrayList<>();

            for (CarritoProducto carritoProducto : carrito.getCarritoProductos()) {
                PedidoProducto pedidoProducto = new PedidoProducto();
                pedidoProducto.setIdPedido(pedidoCreado.getIdPedido());
                pedidoProducto.setIdProducto(carritoProducto.getIdProducto());
                pedidoProducto.setProducto(carritoProducto.getProducto());
                pedidoProducto.setCantidad(carritoProducto.getCantidad());
                pedidoProducto.setPrecio(carritoProducto.getProducto().getPrecio());
                pedidoProducto.setDescuento(carritoProducto.getProducto().getDescuento());
                pedidoProducto.setSubtotal(carritoProducto.getSubtotal());
                pedidoProductos.add(pedidoProductoService.createPedidoProducto(pedidoProducto));
            }

            carritoService.deleteCarrito(carrito.getIdCarrito());
            pedidoCreado.setPedidoProductos(pedidoProductos);

            return ResponseEntity.created(URI.create(""))
                    .body(HttpResponse.builder()
                            .timestamp(now().toString())
                            .data(of("pedido", fromPedido(pedidoCreado, pedidoCreado.getPedidoProductos())))
                            .message("Pedido creado correctamente")
                            .httpStatus(CREATED)
                            .httpStatusCode(CREATED.value())
                            .build()
                    );
        } else {
            return ResponseEntity.badRequest().body(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .message("Carrito no encontrado")
                            .httpStatus(OK)
                            .httpStatusCode(OK.value())
                            .build()
            );
        }
    }

    @PutMapping("/update")
    public ResponseEntity<HttpResponse> updateReview(@RequestBody Pedido pedido) {
        Pedido pedidoActual = pedidoService.getPedidoById(pedido.getIdPedido()).orElse(null);

        if (pedidoActual != null) {
            pedidoActual.setEstado(pedido.getEstado());
            pedidoActual.setFechaModificacion(now());

            return ResponseEntity.ok(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .data(of("pedido", pedidoService.updatePedido(pedidoActual)))
                            .message("Pedido actualizado correctamente")
                            .httpStatus(OK)
                            .httpStatusCode(OK.value())
                            .build()
            );
        } else {
            return ResponseEntity.badRequest().body(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .message("Pedido no encontrado")
                            .httpStatus(OK)
                            .httpStatusCode(OK.value())
                            .build()
            );
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<HttpResponse> deletePedido(@RequestBody Pedido pedido) {
        pedidoProductoService.deleteAllByIdPedido(pedido.getIdPedido());
        pedidoService.deletePedido(pedido.getIdPedido());
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("pedidos", pedidoService.getAllPedidos(0, 10)))
                        .message("Lista de pedidos obtenida correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }
}
