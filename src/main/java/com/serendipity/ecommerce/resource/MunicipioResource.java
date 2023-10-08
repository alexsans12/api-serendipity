package com.serendipity.ecommerce.resource;

import com.serendipity.ecommerce.domain.Departamento;
import com.serendipity.ecommerce.domain.HttpResponse;
import com.serendipity.ecommerce.domain.Municipio;
import com.serendipity.ecommerce.dto.UsuarioDTO;
import com.serendipity.ecommerce.service.MunicipioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/municipio")
@RequiredArgsConstructor
public class MunicipioResource {
    private final MunicipioService municipioService;

    @GetMapping("/list")
    public ResponseEntity<HttpResponse> getMunicipios() {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("municipios", municipioService.getAllMunicipios()))
                        .message("Lista de municipios obtenida correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/departamento/{id}")
    public ResponseEntity<HttpResponse> getMunicipiosByIdDepartamento(@PathVariable Long id) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("municipios", municipioService.getMunicipiosByIdDepartamento(id)))
                        .message("Lista de municipios obtenida correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<HttpResponse> getMunicipio(@PathVariable Long id) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("municipio", municipioService.getMunicipioById(id)))
                        .message("Municipio obtenido correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }

    @PostMapping("/create")
    public ResponseEntity<HttpResponse> createMunicipio(@AuthenticationPrincipal UsuarioDTO usuario, @RequestBody Municipio municipio) {
        return ResponseEntity.created(URI.create(""))
                .body(HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("municipio", municipioService.createMunicipio(municipio)))
                        .message("Municipio creado correctamente")
                        .httpStatus(CREATED)
                        .httpStatusCode(CREATED.value())
                        .build());
    }

    @PutMapping("/update")
    public ResponseEntity<HttpResponse> updateMunicipio(@AuthenticationPrincipal UsuarioDTO usuario, @RequestBody Municipio municipio) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("municipio", municipioService.updateMunicipio(municipio)))
                        .message("Municipio actualizado correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }
}
