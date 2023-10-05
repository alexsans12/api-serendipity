package com.serendipity.ecommerce.resource;

import com.serendipity.ecommerce.domain.Deseados;
import com.serendipity.ecommerce.domain.HttpResponse;
import com.serendipity.ecommerce.domain.Producto;
import com.serendipity.ecommerce.domain.ProductoDeseado;
import com.serendipity.ecommerce.dto.UsuarioDTO;
import com.serendipity.ecommerce.service.DeseadosService;
import com.serendipity.ecommerce.service.ProductoDeseadoService;
import com.serendipity.ecommerce.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.serendipity.ecommerce.dtomapper.DeseadosDTOMapper.fromDeseados;
import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/wish")
@RequiredArgsConstructor
public class DeseadosResource {
    private final DeseadosService deseadosService;
    private final ProductoDeseadoService productoDeseadoService;
    private final ProductoService productoService;

    @GetMapping("/list-all")
    public ResponseEntity<HttpResponse> getDeseados() {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("deseados", deseadosService.getAllListaDeseos()))
                        .message("Lista de deseos obtenida correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<HttpResponse> getDeseadosById(@PathVariable Long id) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("deseados", deseadosService.getListaDeseosById(id)))
                        .message("Lista de deseos obtenida correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/get")
    public ResponseEntity<HttpResponse> getDeseadosByUsuario(@AuthenticationPrincipal UsuarioDTO usuario) {
        Optional<Deseados> listaDeseos = deseadosService.getListaDeseosByUsuario(usuario.getIdUsuario());

        if (listaDeseos.isPresent()) {
            listaDeseos.get().setProductosDeseados((List<ProductoDeseado>) productoDeseadoService.findAllProductoDeseadoByIdDeseados(listaDeseos.get().getIdListaDeseos()));

            return ResponseEntity.ok(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .data(of("deseados", fromDeseados(listaDeseos.get(), listaDeseos.get().getProductosDeseados())))
                            .message("Lista de deseos obtenida correctamente")
                            .httpStatus(OK)
                            .httpStatusCode(OK.value())
                            .build()
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .message("Lista de deseos no encontrada")
                            .httpStatus(OK)
                            .httpStatusCode(OK.value())
                            .build()
            );
        }
    }

    @GetMapping("/producto/{sku}")
    public ResponseEntity<HttpResponse> isProductInDeseados(@AuthenticationPrincipal UsuarioDTO usuario, @PathVariable String sku) {
        Producto producto = productoService.getProductoBySku(sku);
        Optional<ProductoDeseado> productoDeseado = productoDeseadoService.findProductoDeseadoByIdDeseadosAndIdProducto(usuario.getIdUsuario(), producto.getIdProducto());

        if (productoDeseado.isPresent()) {
            return ResponseEntity.ok(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .message("Producto encontrado en la lista de deseos")
                            .httpStatus(OK)
                            .httpStatusCode(OK.value())
                            .build()
            );
        } else {
            return ResponseEntity.ok(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .message("Lista de deseos no encontrada")
                            .httpStatus(OK)
                            .httpStatusCode(OK.value())
                            .build()
            );
        }
    }

    @PostMapping("/create")
    public ResponseEntity<HttpResponse> createListaDeDeseos(@AuthenticationPrincipal UsuarioDTO usuario, @RequestBody List<ProductoDeseado> productoDeseadoList) {
        Deseados deseados = deseadosService.getListaDeseosByUsuario(usuario.getIdUsuario()).orElse(null);
        Deseados finalDeseados = deseados;

        if (deseados == null) {
            deseados = new Deseados();
            deseados.setFechaCreacion(now());
            deseados.setIdUsuario(usuario.getIdUsuario());
            finalDeseados = deseadosService.createListaDeseos(deseados);
        }

        for (ProductoDeseado productoDeseado : productoDeseadoList) {
            productoDeseado.setIdListaDeseos(finalDeseados.getIdListaDeseos());
            productoDeseadoService.createProductoDeseado(productoDeseado);
        }

        finalDeseados.setProductosDeseados((List<ProductoDeseado>) productoDeseadoService.findAllProductoDeseadoByIdDeseados(finalDeseados.getIdListaDeseos()));

        for (ProductoDeseado productoDeseado : finalDeseados.getProductosDeseados()) {
            productoDeseado.setProducto(productoService.getProductoById(productoDeseado.getIdProducto()));
        }

        return ResponseEntity.created(URI.create(""))
                .body(HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("deseados", fromDeseados(finalDeseados, finalDeseados.getProductosDeseados())))
                        .message("Lista de deseos creada correctamente")
                        .httpStatus(CREATED)
                        .httpStatusCode(CREATED.value())
                        .build());
    }

    @PostMapping("/add")
    public ResponseEntity<HttpResponse> addToDeseados(@AuthenticationPrincipal UsuarioDTO usuario, @RequestBody ProductoDeseado productoDeseado) {
        Deseados deseados = deseadosService.getListaDeseosByUsuario(usuario.getIdUsuario()).orElse(null);

        if (deseados == null) {
            deseados = new Deseados();
            deseados.setFechaCreacion(now());
            deseados.setIdUsuario(usuario.getIdUsuario());
            deseados = deseadosService.createListaDeseos(deseados);
        }

        boolean exists = false;

        for (ProductoDeseado existingProductoDeseado : deseados.getProductosDeseados()) {
            if (existingProductoDeseado.getIdProducto().equals(productoDeseado.getIdProducto())) {
                exists = true;
                break;
            }
        }

        if (!exists) {
            productoDeseado.setIdListaDeseos(deseados.getIdListaDeseos());
            productoDeseado = productoDeseadoService.createProductoDeseado(productoDeseado);
            productoDeseado.setProducto(productoService.getProductoById(productoDeseado.getIdProducto()));
            deseados.getProductosDeseados().add(productoDeseado);
        }

        return ResponseEntity.created(URI.create(""))
                .body(HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("deseados", fromDeseados(deseados, deseados.getProductosDeseados())))
                        .message("Producto añadido a la lista de deseos")
                        .httpStatus(CREATED)
                        .httpStatusCode(CREATED.value())
                        .build());
    }

    @PostMapping("/remove")
    public ResponseEntity<HttpResponse> removeFromCart(@AuthenticationPrincipal UsuarioDTO usuario, @RequestBody ProductoDeseado productoDeseado) {
        Deseados deseados = deseadosService.getListaDeseosByUsuario(usuario.getIdUsuario()).orElse(null);
        if (deseados == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .message("Lista de deseos no encontrada")
                            .httpStatus(OK)
                            .httpStatusCode(OK.value())
                            .build()
            );
        }

        for (ProductoDeseado existingProductoDeseado : deseados.getProductosDeseados()) {
            if (existingProductoDeseado.getIdProducto().equals(productoDeseado.getIdProducto())) {
                productoDeseadoService.deleteProductoDeseado(existingProductoDeseado.getIdProductoLista());
            }
        }

        return ResponseEntity.created(URI.create(""))
                .body(HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("deseados", fromDeseados(deseados, (List<ProductoDeseado>) productoDeseadoService.findAllProductoDeseadoByIdDeseados(deseados.getIdListaDeseos()))))
                        .message("Producto eliminado de la lista de deseos")
                        .httpStatus(CREATED)
                        .httpStatusCode(CREATED.value())
                        .build());
    }

    @PutMapping("/update")
    public ResponseEntity<HttpResponse> updateCarrito(@AuthenticationPrincipal UsuarioDTO usuario, @RequestBody List<ProductoDeseado> productosDeseados) {
        Optional<Deseados> deseados = deseadosService.getListaDeseosByUsuario(usuario.getIdUsuario());

        if (deseados.isPresent()) {
            for (ProductoDeseado productoDeseado : deseados.get().getProductosDeseados()) {
                productoDeseadoService.deleteProductoDeseado(productoDeseado.getIdProductoLista());
            }

            List<ProductoDeseado> productoList = new ArrayList<>();

            for (ProductoDeseado productoDeseado : productosDeseados) {
                productoDeseado.setIdListaDeseos(deseados.get().getIdListaDeseos());
                productoList.add(productoDeseadoService.createProductoDeseado(productoDeseado));
            }

            deseados.get().setProductosDeseados(productoList);

            for (ProductoDeseado productoDeseado : deseados.get().getProductosDeseados()) {
                productoDeseado.setProducto(productoService.getProductoById(productoDeseado.getIdProducto()));
            }

            return ResponseEntity.ok(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .data(of("deseados", fromDeseados(deseados.get(), deseados.get().getProductosDeseados())))
                            .message("Lista de deseos actualizada correctamente")
                            .httpStatus(OK)
                            .httpStatusCode(OK.value())
                            .build()
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .data(of("deseados", deseados))
                            .message("Lista de deseos no encontrada")
                            .httpStatus(OK)
                            .httpStatusCode(OK.value())
                            .build()
            );
        }
    }
}
