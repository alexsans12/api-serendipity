package com.serendipity.ecommerce.service;

import com.serendipity.ecommerce.domain.ListaDeseos;

import java.util.Optional;

public interface ListaDeseosService {
    Iterable<ListaDeseos> getAllListaDeseos();

    Optional<ListaDeseos> getListaDeseosById(Long id);
    Optional<ListaDeseos> getListaDeseosByUsuario(Long idUsuario);

    ListaDeseos createListaDeseos(ListaDeseos listaDeseos);

    ListaDeseos updateListaDeseos(ListaDeseos listaDeseos);

    void deleteListaDeseos(Long id);
}
