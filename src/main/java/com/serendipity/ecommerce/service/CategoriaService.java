package com.serendipity.ecommerce.service;

import com.serendipity.ecommerce.domain.Categoria;
import org.springframework.data.domain.Page;

public interface CategoriaService {
    // Categorias functions
    Categoria createCategoria(Categoria categoria);
    Categoria updateCategoria(Categoria categoria);
    Page<Categoria> getCategorias(int page, int size);
    Iterable<Categoria> getCategorias();
    Categoria getCategoriaById(Long id);
    void addProductoToCategoria(Long productoId, Long categoriaId);
}
