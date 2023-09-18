package com.serendipity.ecommerce.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static com.serendipity.ecommerce.dtomapper.MarcaDTOMapper.fromMarca;
import static com.serendipity.ecommerce.dtomapper.UsuarioDTOMapper.toUsuario;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@RestController
@RequestMapping("/api/v1/marca")
@RequiredArgsConstructor
@Slf4j
public class MarcaResource {
    private final MarcaService marcaService;
    private final UsuarioService usuarioService;
    private final ObjectMapper objectMapper;

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
    public ResponseEntity<HttpResponse> createMarca(@AuthenticationPrincipal UsuarioDTO usuario, @RequestParam("image") MultipartFile image, @RequestParam("marca") String jsonData) {
        try {
            Marca marca = objectMapper.readValue(jsonData, Marca.class);
            saveImage(marca.getNombre(), image);
            marca.setUrlImagen(setImageUrl(marca.getNombre()));
            Marca createdMarca = marcaService.createMarca(marca, usuario);
            createdMarca.setCreadoPorUsuario(toUsuario(usuarioService.getUsuarioById(createdMarca.getCreadoPor())));
            return ResponseEntity.created(URI.create(""))
                    .body(HttpResponse.builder()
                            .timestamp(now().toString())
                            .data(of("marca", fromMarca(createdMarca)))
                            .message("Marca creada correctamente")
                            .httpStatus(CREATED)
                            .httpStatusCode(CREATED.value())
                            .build());
        } catch (JsonProcessingException e) {
            throw new ApiException(e.getStackTrace()[0].toString());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<HttpResponse> updateMarca(@AuthenticationPrincipal UsuarioDTO usuario, @RequestParam("image") Optional<MultipartFile> image, @RequestParam("marca") String jsonData) {
        try {
            Marca marca = objectMapper.readValue(jsonData, Marca.class);

            Marca marcaActual = marcaService.getMarcaById(marca.getIdMarca());
            if (!marcaActual.getNombre().equals(marca.getNombre())) {
                deleteImage(marcaActual.getNombre());
            }

            if (image.isPresent()) {
                saveImage(marca.getNombre(), image.get());
                marca.setUrlImagen(setImageUrl(marca.getNombre()));
            }
            marca.setCreadoPor(marcaActual.getCreadoPor());
            marca.setFechaCreacion(marcaActual.getFechaCreacion());
            marca.setModificadoPor(usuario.getIdUsuario());
            marca.setFechaModificacion(now());
            Marca updatedMarca = marcaService.updateMarca(marca);
            updatedMarca.setCreadoPorUsuario(toUsuario(usuarioService.getUsuarioById(updatedMarca.getCreadoPor())));
            updatedMarca.setModificadoPorUsuario(toUsuario(usuarioService.getUsuarioById(updatedMarca.getModificadoPor())));

            return ResponseEntity.ok(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .data(of("marca", fromMarca(updatedMarca)))
                            .message("Marca actualizada correctamente")
                            .httpStatus(OK)
                            .httpStatusCode(OK.value())
                            .build()
            );
        } catch (JsonProcessingException e) {
            throw new ApiException(e.getStackTrace()[0].toString());
        }
    }

    @PatchMapping("/update/image/{id}")
    public ResponseEntity<HttpResponse> updateProfileImage(@AuthenticationPrincipal UsuarioDTO usuarioDTO, @PathVariable Long id, @RequestParam("image") MultipartFile image) {
        Marca marca = marcaService.getMarcaById(id);
        marca.setModificadoPor(usuarioDTO.getIdUsuario());
        marca.setFechaModificacion(now());
        marca.setUrlImagen(setImageUrl(marca.getNombre()));
        saveImage(marca.getNombre(), image);
        marca = marcaService.updateMarca(marca);
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
            throw new ApiException("Error al obtener la imagen de la marca " + fileName);
        }
    }

    private String setImageUrl(String marca) {
        return fromCurrentContextPath()
                .path("/api/v1/marca/image/" + marca.toLowerCase() + ".png" )
                .toUriString();
    }

    private void saveImage(String marca, MultipartFile image) {
        Path fileStorageLocation = Paths.get(System.getProperty("user.home")+"/Downloads/images/marcas/").toAbsolutePath().normalize();
        if(!Files.exists(fileStorageLocation)) {
            try {
                Files.createDirectories(fileStorageLocation);
            } catch (Exception exception) {
                log.error(exception.getMessage());
                throw new ApiException("No se pudo crear el directorio donde se almacenarán los archivos subidos.");
            }
            log.info("Directorio creado con éxito, {}", fileStorageLocation);
        }

        try {
            Files.copy(image.getInputStream(), fileStorageLocation.resolve(marca + ".png"), REPLACE_EXISTING);
        } catch (IOException exception) {
            log.error(exception.getMessage());
            throw new ApiException(exception.getMessage());
        }
        log.info("Imagen guardada con éxito, {}", fileStorageLocation);
    }

    private void deleteImage(String marca) {
        Path fileStorageLocation = Paths.get(System.getProperty("user.home") + "/Downloads/images/marcas/").toAbsolutePath().normalize();
        Path filePath = fileStorageLocation.resolve(marca + ".png");

        try {
            boolean fileDeleted = Files.deleteIfExists(filePath);

            if (fileDeleted) {
                log.info("Imagen eliminada con éxito, {}", filePath);
            } else {
                log.warn("Imagen no encontrada, {}", filePath);
            }
        } catch (IOException exception) {
            log.error(exception.getMessage());
            throw new ApiException(exception.getMessage());
        }
    }
}
