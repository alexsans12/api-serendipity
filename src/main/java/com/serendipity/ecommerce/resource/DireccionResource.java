package com.serendipity.ecommerce.resource;

import com.serendipity.ecommerce.domain.Carrito;
import com.serendipity.ecommerce.domain.CarritoProducto;
import com.serendipity.ecommerce.domain.Direccion;
import com.serendipity.ecommerce.domain.HttpResponse;
import com.serendipity.ecommerce.dto.UsuarioDTO;
import com.serendipity.ecommerce.form.AddressForm;
import com.serendipity.ecommerce.service.DireccionService;
import com.serendipity.ecommerce.service.MunicipioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.serendipity.ecommerce.dtomapper.CarritoDTOMapper.fromCarrito;
import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/direccion")
@RequiredArgsConstructor
public class DireccionResource {
    private final DireccionService direccionService;
    private final MunicipioService municipioService;

    @GetMapping("/get/{id}")
    public ResponseEntity<HttpResponse> getDireccion(@PathVariable Long id) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("direccion", direccionService.getDireccionById(id)))
                        .message("Dirección obtenida correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/get")
    public ResponseEntity<HttpResponse> getDireccionByUsuario(@AuthenticationPrincipal UsuarioDTO usuario) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("direcciones", direccionService.getAllDireccionesByUsuario(usuario.getIdUsuario())))
                        .message("Lista de direcciones obtenida correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }

    @PostMapping("/create")
    public ResponseEntity<HttpResponse> createDepartamento(@AuthenticationPrincipal UsuarioDTO usuario, @RequestBody @Valid AddressForm form) {
        Direccion direccion = new Direccion();
        direccion.setNombre(form.getNombre());
        direccion.setApellido(form.getApellido());
        direccion.setTelefono(form.getTelefono());
        direccion.setMunicipio(municipioService.getMunicipioById(form.getIdMunicipio()).orElse(null));
        direccion.setIdUsuario(usuario.getIdUsuario());
        direccion.setDireccion(form.getDireccion());
        direccion.setIndicaciones(form.getIndicaciones());

        direccionService.createDireccion(direccion);

        return ResponseEntity.created(URI.create(""))
                .body(HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("direcciones", direccionService.getAllDireccionesByUsuario(usuario.getIdUsuario())))
                        .message("Dirección creada correctamente")
                        .httpStatus(CREATED)
                        .httpStatusCode(CREATED.value())
                        .build()
                );
    }

    @PutMapping("/update")
    public ResponseEntity<HttpResponse> updateDepartamento(@AuthenticationPrincipal UsuarioDTO usuario, @RequestBody @Valid AddressForm form) {
        Direccion direccion = direccionService.getDireccionById(form.getIdDireccion()).orElse(null);

        if (direccion != null) {
            direccion.setNombre(form.getNombre());
            direccion.setApellido(form.getApellido());
            direccion.setTelefono(form.getTelefono());
            direccion.setMunicipio(municipioService.getMunicipioById(form.getIdMunicipio()).orElse(null));
            direccion.setDireccion(form.getDireccion());
            direccion.setIndicaciones(form.getIndicaciones());

            direccionService.updateDireccion(direccion);

            return ResponseEntity.ok(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .data(of("direcciones", direccionService.getAllDireccionesByUsuario(usuario.getIdUsuario())))
                            .message("Dirección actualizada correctamente")
                            .httpStatus(OK)
                            .httpStatusCode(OK.value())
                            .build()
            );
        } else {
            return ResponseEntity.badRequest().body(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .message("Dirección no encontrada")
                            .httpStatus(OK)
                            .httpStatusCode(OK.value())
                            .build()
            );
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<HttpResponse> deleteDireccion(@AuthenticationPrincipal UsuarioDTO usuario, @RequestBody Direccion direccion) {
        direccionService.deleteDireccion(direccion.getIdDireccion());
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("direcciones", direccionService.getAllDireccionesByUsuario(usuario.getIdUsuario())))
                        .message("Dirección eliminada correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }
}
