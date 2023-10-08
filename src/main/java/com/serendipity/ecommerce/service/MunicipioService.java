package com.serendipity.ecommerce.service;

import com.serendipity.ecommerce.domain.Departamento;
import com.serendipity.ecommerce.domain.Municipio;

import java.util.Optional;

public interface MunicipioService {
    Iterable<Municipio> getAllMunicipios();
    Optional<Municipio> getMunicipioById(Long id);
    Iterable<Municipio> getMunicipiosByIdDepartamento(Long idDepartamento);
    Municipio createMunicipio(Municipio municipio);
    Municipio updateMunicipio(Municipio municipio);
    void deleteMunicipio(Long id);
}
