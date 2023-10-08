package com.serendipity.ecommerce.repository;

import com.serendipity.ecommerce.domain.Direccion;
import org.springframework.data.repository.CrudRepository;

public interface DireccionRepository extends CrudRepository<Direccion, Long> {
    Iterable<Direccion> findAllByIdUsuario(Long idUsuario);
}
