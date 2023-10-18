package com.serendipity.ecommerce.repository;

import com.serendipity.ecommerce.domain.Deseados;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DeseadosRepository extends CrudRepository<Deseados, Long> {
    Optional<Deseados> findByIdUsuario(Long idUsuario);
}
