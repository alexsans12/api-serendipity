package com.serendipity.ecommerce.repository;

import com.serendipity.ecommerce.domain.ListaDeseos;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ListaDeseosRepository extends CrudRepository<ListaDeseos, Long> {
    Optional<ListaDeseos> findByIdUsuario(Long idUsuario);
}
