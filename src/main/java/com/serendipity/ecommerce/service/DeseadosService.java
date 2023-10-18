package com.serendipity.ecommerce.service;

import com.serendipity.ecommerce.domain.Deseados;

import java.util.Optional;

public interface DeseadosService {
    Iterable<Deseados> getAllListaDeseos();

    Optional<Deseados> getListaDeseosById(Long id);
    Optional<Deseados> getListaDeseosByUsuario(Long idUsuario);

    Deseados createListaDeseos(Deseados deseados);

    Deseados updateListaDeseos(Deseados deseados);

    void deleteListaDeseos(Long id);
}
