package com.serendipity.ecommerce.service;

import com.serendipity.ecommerce.domain.ProductoDeseado;

import java.util.Optional;

public interface ProductoDeseadoService {
    Iterable<ProductoDeseado> getAllProductoDeseado();
    Iterable<ProductoDeseado> findAllProductoDeseadoByIdDeseados(Long idListaDeseos);
    Optional<ProductoDeseado> getProductoDeseadoById(Long id);
    ProductoDeseado createProductoDeseado(ProductoDeseado productoDeseado);
    ProductoDeseado updateProductoDeseado(ProductoDeseado productoDeseado);
    void deleteProductoDeseado(Long id);
    Optional<ProductoDeseado> findProductoDeseadoByIdDeseadosAndIdProducto(Long idListaDeseos, Long idProducto);
}
