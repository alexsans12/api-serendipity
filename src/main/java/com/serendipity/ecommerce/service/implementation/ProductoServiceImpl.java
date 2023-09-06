package com.serendipity.ecommerce.service.implementation;

import com.serendipity.ecommerce.domain.Producto;
import com.serendipity.ecommerce.exception.ApiException;
import com.serendipity.ecommerce.repository.CategoriaRepository;
import com.serendipity.ecommerce.repository.MarcaRepository;
import com.serendipity.ecommerce.repository.ProductoRepository;
import com.serendipity.ecommerce.service.ProductoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.springframework.data.domain.PageRequest.of;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProductoServiceImpl implements ProductoService {
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final MarcaRepository marcaRepository;

    @Override
    public Producto createProducto(Producto producto) {
        // Check if the Categoria and Marca exist
        if (!categoriaRepository.existsById(producto.getIdCategoria()) ||
                !marcaRepository.existsById(producto.getIdMarca())) {
            throw new ApiException("La Categoria o Marca no existe.");
        }

        // Generate SKU based on some rule, for example:
        String sku = generateSku(producto.getIdMarca(), producto.getIdCategoria());

        // Set SKU
        producto.setSku(sku);

        // Save the Producto
        return productoRepository.save(producto);
    }

    @Override
    public Producto updateProducto(Producto producto) {
        // Check if the Producto exists
        Optional<Producto> existingProducto = productoRepository.findById(producto.getIdProducto());
        if (existingProducto.isEmpty()) {
            throw new ApiException("El Producto no existe.");
        }

        // Check if the Categoria and Marca exist
        if (!categoriaRepository.existsById(producto.getIdCategoria()) ||
                !marcaRepository.existsById(producto.getIdMarca())) {
            throw new ApiException("La Categoria o Marca no existe.");
        }

        // Update the Producto
        return productoRepository.save(producto);
    }

    @Override
    public Page<Producto> getProductos(int page, int size) {
        return productoRepository.findAll(of(page, size));
    }

    @Override
    public Iterable<Producto> getProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto getProductoById(Long id) {
        return productoRepository.findById(id).get();
    }

    @Override
    public Page<Producto> searchProductosByName(String name, int page, int size) {
        return productoRepository.findByNombreContaining(name, of(page, size));
    }

    @Override
    public Page<Producto> searchProductosByCategoria(Long categoriaId, int page, int size) {
        if (!categoriaRepository.existsById(categoriaId)) {
            log.error("Categoria with ID {} does not exist.", categoriaId);
            return Page.empty(); // Or throw an exception
        }

        // Fetch the Productos by Categoria ID
        return productoRepository.findByCategoriaIdCategoria(categoriaId, of(page, size));
    }

    @Override
    public Page<Producto> searchProductosByMarca(String marca, int page, int size) {
        return productoRepository.findByMarcaNombreContaining(marca, of(page, size));
    }


    private String generateSku(Long idMarca, Long idCategoria) {
        // Here you can define your logic to generate SKU
        // For example, combining first 2 letters of Marca and first 2 letters of Categoria and a random 8-digit number
        String marcaPrefix = marcaRepository.findById(idMarca).map(m -> m.getNombre().substring(0, 2)).orElse("UN");
        String categoriaPrefix = categoriaRepository.findById(idCategoria).map(c -> c.getNombre().substring(0, 2)).orElse("UN");

        // Generate a unique 8-digit identifier. You can replace this with your logic.
        String uniqueId = randomNumeric(8);

        return (marcaPrefix + categoriaPrefix + uniqueId).toUpperCase();
    }
}
