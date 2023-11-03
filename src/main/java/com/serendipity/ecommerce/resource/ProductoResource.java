package com.serendipity.ecommerce.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serendipity.ecommerce.domain.HttpResponse;
import com.serendipity.ecommerce.domain.ImagenProducto;
import com.serendipity.ecommerce.domain.Producto;
import com.serendipity.ecommerce.dto.UsuarioDTO;
import com.serendipity.ecommerce.dtomapper.ProductoDTOMapper;
import com.serendipity.ecommerce.exception.ApiException;
import com.serendipity.ecommerce.service.CategoriaService;
import com.serendipity.ecommerce.service.ImagenProductoService;
import com.serendipity.ecommerce.service.ProductoService;
import com.serendipity.ecommerce.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.serendipity.ecommerce.dtomapper.ProductoDTOMapper.fromProducto;
import static com.serendipity.ecommerce.dtomapper.UsuarioDTOMapper.toUsuario;
import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/api/v1/producto")
@RequiredArgsConstructor
@Slf4j
public class ProductoResource {
    private final ProductoService productoService;
    private final ImagenProductoService imagenProductoService;
    private final UsuarioService usuarioService;
    private final CategoriaService categoriaService;
    private final ObjectMapper objectMapper;

    private final S3Client s3Client;

    @Value("${do.space.bucket}")
    private String doSpaceBucket;

    @GetMapping("/list")
    public ResponseEntity<HttpResponse> getProductos(@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("productos", productoService.getProductos(page.orElse(0), size.orElse(10)).map(ProductoDTOMapper::fromProducto)))
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
                        .data(of("producto", fromProducto(productoService.getProductoById(id))))
                        .message("Producto obtenido correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<HttpResponse> getProductoBySku(@PathVariable String sku) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("producto", fromProducto(productoService.getProductoBySku(sku))))
                        .message("Producto obtenido correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }

    @PostMapping("/create")
    public ResponseEntity<HttpResponse> createProducto(@AuthenticationPrincipal UsuarioDTO usuarioDTO, @RequestParam("image") MultipartFile[] images, @RequestParam("producto") String jsonData) {
        try {
            Producto producto = objectMapper.readValue(jsonData, Producto.class);
            producto.setCreadoPor(usuarioDTO.getIdUsuario());
            producto.setFechaCreacion(now());
            producto.setEstado(true);
            producto = productoService.createProducto(producto);
            producto.setImagenesProducto(saveImages(producto.getSku(), images, producto.getIdProducto()));
            producto.setCategoria(categoriaService.getCategoriaById(producto.getIdCategoria()));
            producto.setCreadoPorUsuario(toUsuario(usuarioDTO));

            return ResponseEntity.created(URI.create(""))
                    .body(HttpResponse.builder()
                            .timestamp(now().toString())
                            .data(of("producto", fromProducto(producto)))
                            .message("Producto creado exitosamente")
                            .httpStatus(CREATED)
                            .httpStatusCode(CREATED.value())
                            .build());
        } catch (JsonProcessingException e) {
            throw new ApiException(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<HttpResponse> searchProducto(@RequestParam Optional<String> nombre, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("productos", productoService.searchProductosByName(nombre.orElse(""), page.orElse(0), size.orElse(10)).map(ProductoDTOMapper::fromProducto)))
                        .message("Productos obtenidos correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/categoria")
    public ResponseEntity<HttpResponse> searchProductoByCategoria(@RequestParam Optional<String> nombre, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("productos", productoService.searchProductosByCategoria(nombre.orElse(""), page.orElse(0), size.orElse(10)).map(ProductoDTOMapper::fromProducto)))
                        .message("Productos obtenidos correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }

    @PutMapping("/update")
    public ResponseEntity<HttpResponse> updateProducto(@AuthenticationPrincipal UsuarioDTO usuarioDTO, @RequestParam("image") MultipartFile[] images, @RequestParam("producto") String jsonData) {
        try {
            Producto producto = objectMapper.readValue(jsonData, Producto.class);
            Producto existingProducto = productoService.getProductoById(producto.getIdProducto());
            producto.setCreadoPor(existingProducto.getCreadoPor());
            producto.setFechaCreacion(existingProducto.getFechaCreacion());
            producto.setImagenesProducto(saveImages(existingProducto.getSku(), images, producto.getIdProducto()));
            producto.setCategoria(categoriaService.getCategoriaById(producto.getIdCategoria()));
            producto.setModificadoPor(usuarioDTO.getIdUsuario());
            producto.setFechaModificacion(now());

            Producto updateProducto = productoService.updateProducto(producto);
            updateProducto.setSku(existingProducto.getSku());
            updateProducto.setCreadoPor(existingProducto.getCreadoPor());
            updateProducto.setFechaCreacion(existingProducto.getFechaCreacion());
            updateProducto.setCreadoPorUsuario(toUsuario(usuarioService.getUsuarioById(producto.getCreadoPor())));
            updateProducto.setFechaCreacion(producto.getFechaCreacion());
            updateProducto.setModificadoPorUsuario(toUsuario(usuarioService.getUsuarioById(usuarioDTO.getIdUsuario())));
            updateProducto.setImagenesProducto(imagenProductoService.getImagenProductoByIdProducto(updateProducto.getIdProducto()));

            return ResponseEntity.ok(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .data(of("producto", fromProducto(updateProducto)))
                            .message("Producto actualizado correctamente")
                            .httpStatus(OK)
                            .httpStatusCode(OK.value())
                            .build()
            );
        } catch (JsonProcessingException e) {
            throw new ApiException(e.getMessage());
        }
    }

    @PutMapping("/update/stock")
    public ResponseEntity<HttpResponse> updateStockProducto(@AuthenticationPrincipal UsuarioDTO usuarioDTO, @org.springframework.web.bind.annotation.RequestBody Producto producto) {
        Producto productoOriginal = productoService.getProductoById(producto.getIdProducto());
        productoOriginal.setModificadoPor(usuarioDTO.getIdUsuario());
        productoOriginal.setFechaModificacion(now());
        productoOriginal.setCantidad(productoOriginal.getCantidad() + producto.getCantidad());

        Producto updateProducto = productoService.updateProducto(productoOriginal);
        updateProducto.setModificadoPorUsuario(toUsuario(usuarioService.getUsuarioById(usuarioDTO.getIdUsuario())));
        updateProducto.setImagenesProducto(imagenProductoService.getImagenProductoByIdProducto(updateProducto.getIdProducto()));

        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("producto", fromProducto(updateProducto)))
                        .message("Stock actualizado correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }

    @GetMapping(value = "/image/{fileName}", produces = IMAGE_PNG_VALUE)
    public byte[] getProfileImage(@PathVariable("fileName") String fileName) {
        try {
            return Files.readAllBytes(Paths.get("/app/images/producto/" + fileName).toAbsolutePath().normalize());
        } catch (IOException e) {
            throw new ApiException("Error al obtener la imagen del producto con SKU " + fileName);
        }
    }

    /*private String setImageUrl(String nombre) {
        return fromCurrentContextPath()
                .path("/api/v1/producto/image/" + nombre + ".png")
                .toUriString();
    }*/

    private String setImageUrl(String imageName) {
        // Aquí asumimos que todas las imágenes son PNG. Si no es así, ajusta la extensión adecuadamente.
        return "https://" + doSpaceBucket + ".nyc3.digitaloceanspaces.com/producto/" + imageName + ".png";
    }

    /*private void saveImage(String nombre, MultipartFile image) {
        Path fileStorageLocation = Paths.get("/app/images/producto/").toAbsolutePath().normalize();
        if (!Files.exists(fileStorageLocation)) {
            try {
                Files.createDirectories(fileStorageLocation);
            } catch (Exception exception) {
                log.error(exception.getMessage());
                throw new ApiException("No se pudo crear el directorio donde se almacenarán los archivos subidos.");
            }
            log.info("Directorio creado con éxito, {}", fileStorageLocation);
        }

        try {
            Files.copy(image.getInputStream(), fileStorageLocation.resolve(nombre + ".png"), REPLACE_EXISTING);
        } catch (IOException exception) {
            log.error(exception.getMessage());
            throw new ApiException(exception.getMessage());
        }
        log.info("Imagen guardada con éxito, {}", fileStorageLocation);
    }*/

    private void saveImage(String nombre, MultipartFile image) {
        String key = "producto/" + nombre + ".png";

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(doSpaceBucket)
                    .key(key)
                    .contentLength((long) image.getBytes().length)
                    .contentType(image.getContentType())
                    .acl("public-read")
                    .build();

            PutObjectAclRequest aclRequest = PutObjectAclRequest.builder()
                    .bucket(doSpaceBucket)
                    .key(key)
                    .acl("public-read")
                    .build();

            this.s3Client.putObject(putObjectRequest, RequestBody.fromBytes(image.getBytes()));

            log.info("Imagen guardada con éxito en S3, Bucket: {}, Key: {}", doSpaceBucket, key);
        } catch (S3Exception | IOException exception) {
            log.error("Error al guardar imagen en S3", exception);
            throw new ApiException("Error al guardar imagen en S3: " + exception.getMessage());
        }
    }

    /*private void deleteImage(String nombre) {
        Path fileStorageLocation = Paths.get("/app/images/producto/").toAbsolutePath().normalize();
        Path filePath = fileStorageLocation.resolve(nombre + ".png");

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
    }*/

    private void deleteImage(String nombre) {
        String key = "producto/" + nombre + ".png"; // Ruta del archivo en S3

        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(doSpaceBucket)
                    .key(key)
                    .build();

            DeleteObjectResponse response = s3Client.deleteObject(deleteObjectRequest);

            log.info("Imagen eliminada con éxito en S3, Bucket: {}, Key: {}", doSpaceBucket, key);
        } catch (Exception exception) {
            log.error("Error al eliminar imagen en S3", exception);
            throw new ApiException("Error al eliminar imagen en S3: " + exception.getMessage());
        }
    }

    /*private List<ImagenProducto> saveImages(String SKU, MultipartFile[] images, Long idProducto) {
        List<ImagenProducto> imagenesActuales = imagenProductoService.getImagenProductoByIdProducto(idProducto);
        for (ImagenProducto imagenProducto : imagenesActuales) {
            deleteImage(imagenProducto.getUrl().substring(imagenProducto.getUrl().lastIndexOf("/") + 1, imagenProducto.getUrl().length() - 4));
        }
        imagenProductoService.deleteImagenProductoByIdProducto(idProducto);
        List<ImagenProducto> imagenes = new ArrayList<>();
        int iterationCount = 1;
        for (MultipartFile image : images) {
            String nombreImagen = SKU + "_" + iterationCount++;
            saveImage(nombreImagen, image);
            ImagenProducto imagenProducto = new ImagenProducto();
            imagenProducto.setUrl(setImageUrl(nombreImagen));
            imagenProducto.setIdProducto(idProducto);
            imagenes.add(imagenProducto);

            imagenProductoService.createImagenProducto(imagenProducto);
        }
        return imagenes;
    }*/

    private List<ImagenProducto> saveImages(String SKU, MultipartFile[] images, Long idProducto) {
        List<ImagenProducto> imagenesActuales = imagenProductoService.getImagenProductoByIdProducto(idProducto);

        // Elimina imágenes actuales de S3 y registros de la base de datos
        for (ImagenProducto imagenProducto : imagenesActuales) {
            String imageName = imagenProducto.getUrl().substring(imagenProducto.getUrl().lastIndexOf("/") + 1, imagenProducto.getUrl().length() - 4);
            deleteImage(imageName);
        }

        imagenProductoService.deleteImagenProductoByIdProducto(idProducto);

        List<ImagenProducto> imagenes = new ArrayList<>();
        int iterationCount = 1;

        // Guarda nuevas imágenes en S3 y crea nuevos registros en la base de datos
        for (MultipartFile image : images) {
            String nombreImagen = SKU + "_" + iterationCount++;
            saveImage(nombreImagen, image);
            ImagenProducto imagenProducto = new ImagenProducto();
            imagenProducto.setUrl(setImageUrl(nombreImagen));
            imagenProducto.setIdProducto(idProducto);
            imagenes.add(imagenProducto);

            imagenProductoService.createImagenProducto(imagenProducto);
        }

        return imagenes;
    }
}
