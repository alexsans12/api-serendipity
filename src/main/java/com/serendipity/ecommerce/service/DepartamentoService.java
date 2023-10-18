package com.serendipity.ecommerce.service;

import com.serendipity.ecommerce.domain.Departamento;

import java.util.Optional;

public interface DepartamentoService {
    Iterable<Departamento> getAllDepartamentos();
    Optional<Departamento> getDepartamentoById(Long id);
    Departamento createDepartamento(Departamento departamento);
    Departamento updateDepartamento(Departamento departamento);
    void deleteDepartamento(Long id);
}
