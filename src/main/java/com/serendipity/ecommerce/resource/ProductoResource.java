package com.serendipity.ecommerce.resource;

import com.serendipity.ecommerce.domain.HttpResponse;
import com.serendipity.ecommerce.domain.Producto;
import com.serendipity.ecommerce.dto.UsuarioDTO;
import com.serendipity.ecommerce.service.MarcaService;
import com.serendipity.ecommerce.service.ProductoService;
import com.serendipity.ecommerce.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/producto")
@RequiredArgsConstructor
public class ProductoResource {
    private final ProductoService productoService;
    private final UsuarioService usuarioService;
    private final MarcaService marcaService;

    @GetMapping("/list")
    public ResponseEntity<HttpResponse> getProductos(@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("productos", productoService.getProductos(page.orElse(0), size.orElse(10))))
                        .message("Lista de productos obtenida correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<HttpResponse> getProducto(@PathVariable Long id) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("producto", productoService.getProductoById(id)))
                        .message("Producto obtenido correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }

    @PostMapping("/create")
    public ResponseEntity<HttpResponse> createProducto(@AuthenticationPrincipal UsuarioDTO usuario, @RequestBody Producto producto) {
        usuario = usuarioService.getUsuarioByEmail(usuario.getEmail());
        producto.setCreadoPor(usuario.getIdUsuario());
        producto.setEstado(true);
        return ResponseEntity.created(URI.create(""))
                .body(HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("usuario", usuario,
                                "producto", productoService.createProducto(producto)))
                        .message("Producto creado exitosamente")
                        .httpStatus(CREATED)
                        .httpStatusCode(CREATED.value())
                        .build());
    }

    @GetMapping("/search")
    public ResponseEntity<HttpResponse> searchProducto(@RequestParam Optional<String> nombre, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("productos", productoService.searchProductosByName(nombre.orElse(""), page.orElse(0), size.orElse(10))))
                        .message("Productos obtenidos correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/marca/{nombre}")
    public ResponseEntity<HttpResponse> searchProductoByMarca(@PathVariable String nombre, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("productos", productoService.searchProductosByMarca(nombre, page.orElse(0), size.orElse(10))))
                        .message("Productos obtenidos correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }

    @PutMapping("/update")
    public ResponseEntity<HttpResponse> updateProducto(@AuthenticationPrincipal UsuarioDTO usuario, @RequestBody Producto producto) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("usuario", usuarioService.getUsuarioByEmail(usuario.getEmail()),
                                "producto", productoService.updateProducto(producto)))
                        .message("Producto actualizado correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }
}
