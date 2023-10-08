package com.serendipity.ecommerce.service;

import com.serendipity.ecommerce.domain.Direccion;

import java.util.Optional;

public interface DireccionService {
    Iterable<Direccion> getAllDirecciones();

    Iterable<Direccion> getAllDireccionesByUsuario(Long idUsuario);

    Optional<Direccion> getDireccionById(Long id);

    Direccion createDireccion(Direccion direccion);

    Direccion updateDireccion(Direccion direccion);

    void deleteDireccion(Long id);
}
