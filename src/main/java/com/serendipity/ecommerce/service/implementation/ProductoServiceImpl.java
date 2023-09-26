package com.serendipity.ecommerce.service.implementation;

import com.serendipity.ecommerce.domain.Producto;
import com.serendipity.ecommerce.exception.ApiException;
import com.serendipity.ecommerce.repository.CategoriaRepository;
import com.serendipity.ecommerce.repository.ProductoRepository;
import com.serendipity.ecommerce.service.ProductoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Override
    public Producto createProducto(Producto producto) {
        // Check if the Categoria exist
        if (!categoriaRepository.existsById(producto.getIdCategoria())) throw new ApiException("La Categoria con ID " + producto.getIdCategoria() + " no existe.");
        // Generate SKU based on some rule, for example:
        String sku = generateSku(producto.getIdCategoria());
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
    public Producto getProductoBySku(String sku) {
        return productoRepository.findBySku(sku);
    }

    @Override
    public Page<Producto> searchProductosByName(String name, int page, int size) {
        return productoRepository.findByNombreContaining(name, of(page, size));
    }

    @Override
    public Page<Producto> searchProductosByCategoria(String categoria, int page, int size) {
        // Fetch the Productos by Categoria ID
        return productoRepository.findByCategoriaNombre(categoria, of(page, size));
    }

    @Override
    public void deleteProducto(Long id) {
        productoRepository.delete(getProductoById(id));
    }

    @Override
    public boolean checkStock(Long id) {
        return productoRepository.checkStock(id);
    }

    @Override
    public void updateStock(Long id, int cantidad) {
        productoRepository.updateStock(id, cantidad);
    }

    private String generateSku(Long idCategoria) {
        // Here you can define your logic to generate SKU
        // For example, combining first 4 letters of Categoria and a random 8-digit number
        String categoriaPrefix = categoriaRepository.findById(idCategoria).map(c -> c.getNombre().substring(0, 4)).orElse("UN");

        // Generate a unique 8-digit identifier. You can replace this with your logic.
        String uniqueId = randomNumeric(8);

        return (categoriaPrefix + uniqueId).toUpperCase();
    }
}
