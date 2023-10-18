package com.serendipity.ecommerce.service;

import com.serendipity.ecommerce.domain.Categoria;
import com.serendipity.ecommerce.dto.CategoriaHierarchyDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoriaService {
    // Categorias functions
    Categoria createCategoria(Categoria categoria);
    Categoria updateCategoria(Categoria categoria);
    Page<Categoria> getCategorias(int page, int size);
    Iterable<Categoria> getCategorias();
    Categoria getCategoriaById(Long id);
    void addProductoToCategoria(Long productoId, Long categoriaId);
    List<CategoriaHierarchyDTO> getCategoriasConHijas();
}
