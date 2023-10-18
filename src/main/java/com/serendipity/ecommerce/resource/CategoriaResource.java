package com.serendipity.ecommerce.resource;

import com.serendipity.ecommerce.domain.Categoria;
import com.serendipity.ecommerce.domain.HttpResponse;
import com.serendipity.ecommerce.dto.UsuarioDTO;
import com.serendipity.ecommerce.dtomapper.CategoriaDTOMapper;
import com.serendipity.ecommerce.service.CategoriaService;
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
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoriaResource {
    private final CategoriaService categoriaService;

    @GetMapping("/list-all")
    public ResponseEntity<HttpResponse> getCategorias(@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("categorias", categoriaService.getCategorias(page.orElse(0), size.orElse(10)).map(CategoriaDTOMapper::fromCategoria)))
                        .message("Lista de categorias obtenida correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/list")
    public ResponseEntity<HttpResponse> getCategoriasWithChilds() {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("categorias", categoriaService.getCategoriasConHijas()))
                        .message("Lista de categorias obtenida correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<HttpResponse> getCategoria(@PathVariable Long id) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("categoria", categoriaService.getCategoriaById(id)))
                        .message("Categoria obtenida correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }

    @PostMapping("/create")
    public ResponseEntity<HttpResponse> createCategoria(@AuthenticationPrincipal UsuarioDTO usuario, @RequestBody Categoria categoria) {
        categoria.setCreadoPor(usuario.getIdUsuario());
        categoria.setFechaCreacion(now());
        return ResponseEntity.created(URI.create(""))
                .body(HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("categoria", categoriaService.createCategoria(categoria)))
                        .message("Categoria creada correctamente")
                        .httpStatus(CREATED)
                        .httpStatusCode(CREATED.value())
                        .build());
    }

    @PutMapping("/update")
    public ResponseEntity<HttpResponse> updateCategoria(@AuthenticationPrincipal UsuarioDTO usuario, @RequestBody Categoria categoria) {
        categoria.setModificadoPor(usuario.getIdUsuario());
        categoria.setFechaModificacion(now());
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("categoria", categoriaService.updateCategoria(categoria)))
                        .message("Categoria actualizada correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }
}
