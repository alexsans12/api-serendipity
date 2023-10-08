package com.serendipity.ecommerce.repository;

import com.serendipity.ecommerce.domain.Municipio;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MunicipioRepository extends CrudRepository<Municipio, Long> {
    @Query("SELECT m FROM Municipio m WHERE m.departamento.idDepartamento = ?1")
    Iterable<Municipio> findMunicipiosByIdDepartamento(Long idDepartamento);
}
