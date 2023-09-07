package com.serendipity.ecommerce.resource;

import com.serendipity.ecommerce.domain.HttpResponse;
import com.serendipity.ecommerce.domain.Marca;
import com.serendipity.ecommerce.dto.UsuarioDTO;
import com.serendipity.ecommerce.exception.ApiException;
import com.serendipity.ecommerce.service.MarcaService;
import com.serendipity.ecommerce.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import static com.serendipity.ecommerce.dtomapper.MarcaDTOMapper.fromMarca;
import static com.serendipity.ecommerce.dtomapper.MarcaDTOMapper.toMarca;
import static com.serendipity.ecommerce.dtomapper.UsuarioDTOMapper.toUsuario;
import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/api/v1/marca")
@RequiredArgsConstructor
@Slf4j
public class MarcaResource {
    private final MarcaService marcaService;
    private final UsuarioService usuarioService;

    @GetMapping("/list")
    public ResponseEntity<HttpResponse> getMarcas(@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("marcas", marcaService.getMarcas(page.orElse(0), size.orElse(10))))
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
        return ResponseEntity.created(URI.create(""))
                .body(HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("marca", marcaService.createMarca(marca, usuario)))
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
                        .data(of("marca", marcaService.updateMarca(marca)))
                        .message("Marca actualizada correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }

    @PatchMapping("/update/image/{id}")
    public ResponseEntity<HttpResponse> updateProfileImage(@AuthenticationPrincipal UsuarioDTO usuarioDTO, @PathVariable Long id, @RequestParam("image") MultipartFile image) {
        Marca marca = marcaService.getMarcaById(id);
        marca.setModificadoPor(usuarioDTO.getIdUsuario());
        marca.setFechaModificacion(now());
        marca = marcaService.updateImage(marca, image);
        marca.setModificadoPorUsuario(toUsuario(usuarioDTO));
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .data(of("marca", fromMarca(marca)))
                        .message("Imagen de la marca actualizada exitosamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build());
    }

    @GetMapping(value = "/image/{fileName}", produces = IMAGE_PNG_VALUE)
    public byte[] getProfileImage(@PathVariable("fileName") String fileName) {
        try {
            return Files.readAllBytes(Paths.get(System.getProperty("user.home") + "/Downloads/images/marcas/" + fileName));
        } catch (IOException e) {
            throw new ApiException("Error al obtener la imagen del usuario " + fileName);
        }
    }
}
