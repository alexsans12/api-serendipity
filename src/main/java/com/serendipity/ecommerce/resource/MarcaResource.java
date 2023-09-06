package com.serendipity.ecommerce.resource;

import com.serendipity.ecommerce.domain.HttpResponse;
import com.serendipity.ecommerce.domain.Marca;
import com.serendipity.ecommerce.dto.UsuarioDTO;
import com.serendipity.ecommerce.dtomapper.MarcaDTOMapper;
import com.serendipity.ecommerce.service.MarcaService;
import com.serendipity.ecommerce.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

import static com.serendipity.ecommerce.dtomapper.MarcaDTOMapper.fromMarca;
import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/marca")
@RequiredArgsConstructor
public class MarcaResource {
    private final MarcaService marcaService;
    private final UsuarioService usuarioService;

    @GetMapping("/list")
    public ResponseEntity<HttpResponse> getMarcas(@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("marcas", marcaService.getMarcas(page.orElse(0), size.orElse(10)).map(MarcaDTOMapper::fromMarca)))
                        .message("Lista de marcas obtenida correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<HttpResponse> getMarca(@PathVariable Long id) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("marca", fromMarca(marcaService.getMarcaById(id))))
                        .message("Marca obtenida correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }

    @PostMapping("/create")
    public ResponseEntity<HttpResponse> createMarca(@AuthenticationPrincipal UsuarioDTO usuario, @RequestBody Marca marca) {
        marca.setCreadoPor(usuario.getIdUsuario());
        marca.setEstado(true);
        marca.setFechaCreacion(now());
        return ResponseEntity.created(URI.create(""))
                .body(HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("usuario", usuarioService.getUsuarioByEmail(usuario.getEmail()),
                                "marca", marcaService.createMarca(marca)))
                        .message("Marca creada correctamente")
                        .httpStatus(CREATED)
                        .httpStatusCode(CREATED.value())
                        .build());
    }

    @PutMapping("/update")
    public ResponseEntity<HttpResponse> updateMarca(@AuthenticationPrincipal UsuarioDTO usuario, @RequestBody Marca marca) {
        marca.setModificadoPor(usuario.getIdUsuario());
        marca.setFechaModificacion(now());
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("usuario", usuarioService.getUsuarioByEmail(usuario.getEmail()),
                                "marca", marcaService.updateMarca(marca)))
                        .message("Marca actualizada correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }
}
