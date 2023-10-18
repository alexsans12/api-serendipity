package com.serendipity.ecommerce.service;

import com.serendipity.ecommerce.domain.Producto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductoService {
    Producto createProducto(Producto producto);
    Producto updateProducto(Producto producto);
    Page<Producto> getProductos(int page, int size);
    Iterable<Producto> getProductos();
    Producto getProductoById(Long id);
    Producto getProductoBySku(String sku);
    Page<Producto> searchProductosByName(String name, int page, int size);
    Page<Producto> searchProductosByCategoria(String categoria, int page, int size);
    void deleteProducto(Long id);
    boolean checkStock(Long id);
    void updateStock(Long id, int cantidad);
}
