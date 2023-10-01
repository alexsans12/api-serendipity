package com.serendipity.ecommerce.repository;

import com.serendipity.ecommerce.domain.CarritoProducto;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProductoLista extends CrudRepository<ProductoLista, Long> {
    @Query("SELECT pl FROM ProductoLista pl WHERE pl.idListaDeseos = ?1")
    Iterable<CarritoProducto> findAllProductosListaByIdListaDeseos(Long idListaDeseos);
    @Modifying
    @Query("DELETE FROM ProductoLista pl WHERE pl.idListaDeseos = ?1")
    @Transactional
    void deleteByIdListaDeseos (Long idListaDeseos);
    Optional<CarritoProducto> findByIdListaDeseosAndIdProducto(Long idListaDeseos, Long idProducto);
}
