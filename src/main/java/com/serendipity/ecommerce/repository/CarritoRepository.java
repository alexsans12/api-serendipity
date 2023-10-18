package com.serendipity.ecommerce.repository;

import com.serendipity.ecommerce.domain.Carrito;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CarritoRepository extends CrudRepository<Carrito, Long> {
    Optional<Carrito> findByIdUsuario(Long idUsuario);
}
