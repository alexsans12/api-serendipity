package com.serendipity.ecommerce.resource;

import com.serendipity.ecommerce.domain.Carrito;
import com.serendipity.ecommerce.domain.CarritoProducto;
import com.serendipity.ecommerce.domain.HttpResponse;
import com.serendipity.ecommerce.dto.UsuarioDTO;
import com.serendipity.ecommerce.service.CarritoProductoService;
import com.serendipity.ecommerce.service.CarritoService;
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

import static com.serendipity.ecommerce.dtomapper.CarritoDTOMapper.fromCarrito;
import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CarritoResource {
    private final CarritoService carritoService;
    private final CarritoProductoService carritoProductoService;
    private final ProductoService productoService;

    @GetMapping("/list-all")
    public ResponseEntity<HttpResponse> getCarritos() {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("carritos", carritoService.getAllCarritos()))
                        .message("Lista de carritos obtenida correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<HttpResponse> getCarrito(@PathVariable Long id) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("carrito", carritoService.getCarritoById(id)))
                        .message("Carrito obtenido correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/get")
    public ResponseEntity<HttpResponse> getCarritoByUsuario(@AuthenticationPrincipal UsuarioDTO usuario) {
        Optional<Carrito> carrito = carritoService.getCarritoByUsuario(usuario.getIdUsuario());

        if (carrito.isPresent()) {
            carrito.get().setCarritoProductos((List<CarritoProducto>) carritoProductoService.findAllCarritoProductosByIdCarrito(carrito.get().getIdCarrito()));

            return ResponseEntity.ok(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .data(of("carrito", fromCarrito(carrito.get(), carrito.get().getCarritoProductos())))
                            .message("Carrito obtenido correctamente")
                            .httpStatus(OK)
                            .httpStatusCode(OK.value())
                            .build()
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .message("Carrito no encontrado")
                            .httpStatus(OK)
                            .httpStatusCode(OK.value())
                            .build()
            );
        }
    }

    @PostMapping("/create")
    public ResponseEntity<HttpResponse> createCarrito(@AuthenticationPrincipal UsuarioDTO usuario, @RequestBody List<CarritoProducto> carritoProductos) {
        Carrito carrito = carritoService.getCarritoByUsuario(usuario.getIdUsuario()).orElse(null);
        Carrito finalCarrito = carrito;

        if (carrito == null) {
            carrito = new Carrito();
            carrito.setFechaCreacion(now());
            carrito.setIdUsuario(usuario.getIdUsuario());
            finalCarrito = carritoService.createCarrito(carrito);
        }

        for (CarritoProducto carritoProducto : carritoProductos) {
            Optional<CarritoProducto> existingCarritoProductoOpt = carritoProductoService.findCarritoProductoByIdCarritoAndIdProducto(
                    finalCarrito.getIdCarrito(), carritoProducto.getIdProducto()
            );

            // Verifica si el Optional contiene un valor antes de acceder a él
            if (existingCarritoProductoOpt.isPresent()) {
                CarritoProducto existingCarritoProducto = existingCarritoProductoOpt.get();
                // Si existe, actualiza la cantidad
                existingCarritoProducto.setCantidad(existingCarritoProducto.getCantidad() + carritoProducto.getCantidad());
                carritoProductoService.updateCarritoProducto(existingCarritoProducto);
            } else {
                // Si no existe, crea un nuevo CarritoProducto
                carritoProducto.setIdCarrito(finalCarrito.getIdCarrito());
                carritoProductoService.createCarritoProducto(carritoProducto);
            }
        }

        finalCarrito.setCarritoProductos((List<CarritoProducto>) carritoProductoService.findAllCarritoProductosByIdCarrito(finalCarrito.getIdCarrito()));

        for (CarritoProducto carritoProducto : finalCarrito.getCarritoProductos()) {
            carritoProducto.setProducto(productoService.getProductoById(carritoProducto.getIdProducto()));
        }

        return ResponseEntity.created(URI.create(""))
                .body(HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("carrito", fromCarrito(finalCarrito, finalCarrito.getCarritoProductos())))
                        .message("Carrito creado correctamente")
                        .httpStatus(CREATED)
                        .httpStatusCode(CREATED.value())
                        .build());
    }

    @PostMapping("/add")
    public ResponseEntity<HttpResponse> addToCart(@AuthenticationPrincipal UsuarioDTO usuario, @RequestBody CarritoProducto carritoProducto) {
        Carrito carrito = carritoService.getCarritoByUsuario(usuario.getIdUsuario()).orElse(null);

        if (carrito == null) {
            carrito = new Carrito();
            carrito.setFechaCreacion(now());
            carrito.setIdUsuario(usuario.getIdUsuario());
            carrito.setCarritoProductos(new ArrayList<>());
            carrito = carritoService.createCarrito(carrito);
        }

        boolean exists = false;

        for (CarritoProducto existingCarritoProducto : carrito.getCarritoProductos()) {
            if (existingCarritoProducto.getIdProducto().equals(carritoProducto.getIdProducto())) {
                existingCarritoProducto.setCantidad(existingCarritoProducto.getCantidad() + carritoProducto.getCantidad());
                carritoProductoService.updateCarritoProducto(existingCarritoProducto);
                exists = true;
                break;
            }
        }

        if (!exists) {
            carritoProducto.setIdCarrito(carrito.getIdCarrito());
            carritoProducto = carritoProductoService.createCarritoProducto(carritoProducto);
            carritoProducto.setProducto(productoService.getProductoById(carritoProducto.getIdProducto()));
            carrito.getCarritoProductos().add(carritoProducto);
        }

        return ResponseEntity.created(URI.create(""))
                .body(HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("carrito", fromCarrito(carrito, carrito.getCarritoProductos())))
                        .message("Producto agregado al carrito")
                        .httpStatus(CREATED)
                        .httpStatusCode(CREATED.value())
                        .build());
    }

    @PostMapping("/remove")
    public ResponseEntity<HttpResponse> removeFromCart(@AuthenticationPrincipal UsuarioDTO usuario, @RequestBody CarritoProducto carritoProducto) {
        Carrito carrito = carritoService.getCarritoByUsuario(usuario.getIdUsuario()).orElse(null);
        if (carrito == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .message("Carrito no encontrado")
                            .httpStatus(OK)
                            .httpStatusCode(OK.value())
                            .build()
            );
        }

        for (CarritoProducto existingCarritoProducto : carrito.getCarritoProductos()) {
            if (existingCarritoProducto.getIdProducto().equals(carritoProducto.getIdProducto())) {
                if (existingCarritoProducto.getCantidad() == 1 || existingCarritoProducto.getCantidad() <= carritoProducto.getCantidad()) {
                    carritoProductoService.deleteCarritoProducto(existingCarritoProducto.getIdCarritoProducto());
                } else {
                    existingCarritoProducto.setCantidad(existingCarritoProducto.getCantidad() - carritoProducto.getCantidad());
                    carritoProductoService.updateCarritoProducto(existingCarritoProducto);
                    break;
                }
            }
        }

        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("carrito", fromCarrito(carrito, (List<CarritoProducto>) carritoProductoService.findAllCarritoProductosByIdCarrito(carrito.getIdCarrito()))))
                        .message("Eliminado del carrito")
                        .httpStatus(CREATED)
                        .httpStatusCode(CREATED.value())
                        .build()
        );
    }

    @PutMapping("/update")
    public ResponseEntity<HttpResponse> updateCarrito(@AuthenticationPrincipal UsuarioDTO usuario, @RequestBody List<CarritoProducto> carritoProductos) {
        Optional<Carrito> carrito = carritoService.getCarritoByUsuario(usuario.getIdUsuario());

        if (carrito.isPresent()) {
            // Delete CarritoProductos that are no longer in the updated list
            for (CarritoProducto carritoProducto : carrito.get().getCarritoProductos()) {
                carritoProductoService.deleteCarritoProducto(carritoProducto.getIdCarritoProducto());
            }

            List<CarritoProducto> productoList = new ArrayList<>();

            for (CarritoProducto carritoProducto : carritoProductos) {
                carritoProducto.setIdCarrito(carrito.get().getIdCarrito());
                productoList.add(carritoProductoService.createCarritoProducto(carritoProducto));
            }

            carrito.get().setCarritoProductos(productoList);

            for (CarritoProducto carritoProducto : carrito.get().getCarritoProductos()) {
                carritoProducto.setProducto(productoService.getProductoById(carritoProducto.getIdProducto()));
            }

            return ResponseEntity.ok(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .data(of("carrito", fromCarrito(carrito.get(), carrito.get().getCarritoProductos())))
                            .message("Carrito actualizado correctamente")
                            .httpStatus(OK)
                            .httpStatusCode(OK.value())
                            .build()
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .data(of("carrito", carrito))
                            .message("Carrito no encontrado")
                            .httpStatus(OK)
                            .httpStatusCode(OK.value())
                            .build()
            );
        }
    }
}
