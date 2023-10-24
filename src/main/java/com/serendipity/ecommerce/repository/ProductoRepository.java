package com.serendipity.ecommerce.repository;

import com.serendipity.ecommerce.domain.Producto;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductoRepository extends PagingAndSortingRepository<Producto, Long>, ListCrudRepository<Producto, Long> {
    Producto findBySku(String sku);
    @Query("SELECT p FROM Producto p WHERE p.nombre LIKE %?1%")
    Page<Producto> findByNombreContaining(String name, Pageable pageable);

    @Query(value = "SELECT p.* FROM producto p " +
            "WHERE p.id_categoria IN (SELECT c.id_categoria FROM categoria c WHERE c.nombre = :categoriaNombre) " +
            "OR p.id_categoria IN (SELECT c.id_categoria FROM categoria c WHERE c.id_categoria_padre = (SELECT c.id_categoria FROM categoria c WHERE c.id_categoria_padre IS NULL AND c.nombre = :categoriaNombre))",
            nativeQuery = true)
    Page<Producto> findByCategoriaNombre(@Param("categoriaNombre") String categoriaNombre, Pageable pageable);

    @Query("SELECT CASE WHEN p.cantidad > 0 THEN true ELSE false END FROM Producto p WHERE p.idProducto = ?1")
    boolean checkStock(Long id);
    @Transactional
    @Modifying
    @Query("UPDATE Producto p SET p.cantidad = p.cantidad + ?2 WHERE p.idProducto = ?1")
    void updateStock(Long id, int cantidad);
}
