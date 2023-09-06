package com.serendipity.ecommerce.repository;

import com.serendipity.ecommerce.domain.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductoRepository extends PagingAndSortingRepository<Producto, Long>, ListCrudRepository<Producto, Long> {
    Page<Producto> findByNombreContaining(String name, Pageable pageable);
    Page<Producto> findByCategoriaIdCategoria(Long categoriaId, Pageable pageable);
    Page<Producto> findByMarcaNombreContaining(String marca, Pageable pageable);
}
