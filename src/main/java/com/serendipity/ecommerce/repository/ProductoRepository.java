package com.serendipity.ecommerce.repository;

import com.serendipity.ecommerce.domain.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductoRepository extends PagingAndSortingRepository<Producto, Long>, ListCrudRepository<Producto, Long> {
    Page<Producto> findByNombreContaining(String name, Pageable pageable);
    @Query("SELECT p FROM Producto p WHERE p.idCategoria = ?1")
    Page<Producto> findByCategoriaIdCategoria(Long categoriaId, Pageable pageable);
    @Query("SELECT p FROM Producto p WHERE p.idMarca = (SELECT m.idMarca FROM Marca m WHERE m.nombre = ?1)")
    Page<Producto> findByMarcaNombreContaining(String marca, Pageable pageable);
}
