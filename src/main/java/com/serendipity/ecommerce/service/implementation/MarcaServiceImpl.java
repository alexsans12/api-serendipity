package com.serendipity.ecommerce.service.implementation;

import com.serendipity.ecommerce.domain.Marca;
import com.serendipity.ecommerce.domain.Producto;
import com.serendipity.ecommerce.dto.MarcaDTO;
import com.serendipity.ecommerce.dto.UsuarioDTO;
import com.serendipity.ecommerce.dtomapper.MarcaDTOMapper;
import com.serendipity.ecommerce.exception.ApiException;
import com.serendipity.ecommerce.repository.MarcaRepository;
import com.serendipity.ecommerce.repository.ProductoRepository;
import com.serendipity.ecommerce.service.MarcaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.serendipity.ecommerce.dtomapper.MarcaDTOMapper.fromMarca;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.time.LocalDateTime.now;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MarcaServiceImpl implements MarcaService {
    private final MarcaRepository marcaRepository;
    private final ProductoRepository productoRepository;

    @Override
    public Marca createMarca(Marca marca, UsuarioDTO usuario) {
        marca.setCreadoPor(usuario.getIdUsuario());
        marca.setEstado(true);
        marca.setFechaCreacion(now());
        return marcaRepository.save(marca);
    }

    @Override
    public Marca updateMarca(Marca marca) {
        log.info(marca.getCreadoPorUsuario().getNombre());
        return marcaRepository.save(marca);
    }

    @Override
    public Page<MarcaDTO> getMarcas(int page, int size) {
        return marcaRepository.findAll(of(page, size)).map(MarcaDTOMapper::fromMarca);
    }

    @Override
    public Iterable<Marca> getMarcas() {
        return marcaRepository.findAll();
    }

    @Override
    public Marca getMarcaById(Long id) {
        return marcaRepository.findById(id).get();
    }

    @Override
    public Marca getMarcaByNombre(String nombre) {
        return marcaRepository.findByNombre(nombre);
    }

    @Override
    public void addProductoToMarca(Long productoId, Long marcaId) {
        Producto producto = productoRepository.findById(productoId).get();
        marcaRepository.findById(marcaId).ifPresent(marca -> {
            producto.setMarca(marca);
            producto.setIdMarca(marca.getIdMarca());
            producto.setFechaModificacion(now());
            productoRepository.save(producto);
        });
    }

    @Override
    public Marca updateImage(Marca marca, MultipartFile image) {
        String urlFoto = setImageUrl(marca.getNombre());
        marca.setUrlImagen(urlFoto);
        saveImage(marca.getNombre(), image);
        return marcaRepository.save(marca);
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
}
