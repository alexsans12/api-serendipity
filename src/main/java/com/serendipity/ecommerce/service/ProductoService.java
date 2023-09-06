package com.serendipity.ecommerce.service;

import com.serendipity.ecommerce.domain.Producto;
import org.springframework.data.domain.Page;

public interface ProductoService {
    Producto createProducto(Producto producto);
    Producto updateProducto(Producto producto);
    Page<Producto> getProductos(int page, int size);
    Iterable<Producto> getProductos();
    Producto getProductoById(Long id);
    Page<Producto> searchProductosByName(String name, int page, int size);
    Page<Producto> searchProductosByCategoria(Long categoriaId, int page, int size);
    Page<Producto> searchProductosByMarca(String marca, int page, int size);
}
