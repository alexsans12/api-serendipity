package com.serendipity.ecommerce.repository;

import com.serendipity.ecommerce.domain.CarritoProducto;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CarritoProductoRepository extends CrudRepository<CarritoProducto, Long> {
    @Query("SELECT cp FROM CarritoProducto cp WHERE cp.idCarrito = ?1")
    Iterable<CarritoProducto> findAllCarritoProductosByIdCarrito(Long idCarrito);
    @Modifying
    @Query("DELETE FROM CarritoProducto cp WHERE cp.idCarritoProducto = ?1")
    @Transactional
    void deleteByIdCarritoProducto (Long idCarritoProducto);
    Optional<CarritoProducto> findByIdCarritoAndIdProducto(Long idCarrito, Long idProducto);
}
