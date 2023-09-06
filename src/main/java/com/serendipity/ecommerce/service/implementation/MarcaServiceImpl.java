package com.serendipity.ecommerce.service.implementation;

import com.serendipity.ecommerce.domain.Marca;
import com.serendipity.ecommerce.domain.Producto;
import com.serendipity.ecommerce.dto.MarcaDTO;
import com.serendipity.ecommerce.dtomapper.MarcaDTOMapper;
import com.serendipity.ecommerce.repository.MarcaRepository;
import com.serendipity.ecommerce.repository.ProductoRepository;
import com.serendipity.ecommerce.service.MarcaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import static java.time.LocalDateTime.now;
import static org.springframework.data.domain.PageRequest.of;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MarcaServiceImpl implements MarcaService {
    private final MarcaRepository marcaRepository;
    private final ProductoRepository productoRepository;
    @Override
    public Marca createMarca(Marca marca) {
        return marcaRepository.save(marca);
    }

    @Override
    public Marca updateMarca(Marca marca) {
        return marcaRepository.save(marca);
    }

    @Override
    public Page<Marca> getMarcas(int page, int size) {
        return marcaRepository.findAll(of(page, size));
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
}
