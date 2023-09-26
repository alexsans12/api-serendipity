package com.serendipity.ecommerce.repository;

import com.serendipity.ecommerce.domain.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ProductoRepository extends PagingAndSortingRepository<Producto, Long>, ListCrudRepository<Producto, Long> {
    Producto findBySku(String sku);
    Page<Producto> findByNombreContaining(String name, Pageable pageable);
    @Query("SELECT p FROM Producto p WHERE p.idCategoria = (SELECT c.idCategoria FROM Categoria c WHERE c.nombre = ?1)")
    Page<Producto> findByCategoriaNombre(String categoria, Pageable pageable);
    @Query("SELECT CASE WHEN p.cantidad > 0 THEN true ELSE false END FROM Producto p WHERE p.idProducto = ?1")
    boolean checkStock(Long id);
    @Query("UPDATE Producto p SET p.cantidad = p.cantidad + ?2 WHERE p.idProducto = ?1")
    void updateStock(Long id, int cantidad);
}
