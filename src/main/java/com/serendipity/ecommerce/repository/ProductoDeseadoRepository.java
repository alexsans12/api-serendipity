package com.serendipity.ecommerce.repository;

import com.serendipity.ecommerce.domain.CarritoProducto;
import com.serendipity.ecommerce.domain.ProductoDeseado;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProductoDeseadoRepository extends CrudRepository<ProductoDeseado, Long> {
    @Query("SELECT pl FROM ProductoDeseado pl WHERE pl.idListaDeseos = ?1")
    Iterable<ProductoDeseado> findAllProductosDeseadoByIdListaDeseos(Long idListaDeseos);
    @Modifying
    @Transactional
    @Query("DELETE FROM ProductoDeseado pl WHERE pl.idListaDeseos = ?1")
    void deleteByIdListaDeseos (Long idListaDeseos);
    @Modifying
    @Transactional
    @Query("DELETE FROM ProductoDeseado pl WHERE pl.idProductoLista = ?1")
    void deleteById (Long id);
    Optional<ProductoDeseado> findByIdListaDeseosAndIdProducto(Long idListaDeseos, Long idProducto);
}
