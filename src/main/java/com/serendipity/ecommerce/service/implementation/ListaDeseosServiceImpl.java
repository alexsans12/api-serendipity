package com.serendipity.ecommerce.service.implementation;

import com.serendipity.ecommerce.domain.ListaDeseos;
import com.serendipity.ecommerce.service.ListaDeseosService;

import java.util.Optional;

public class ListaDeseosServiceImpl implements ListaDeseosService {

    @Override
    public Iterable<ListaDeseos> getAllListaDeseos() {
        return null;
    }

    @Override
    public Optional<ListaDeseos> getListaDeseosById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<ListaDeseos> getListaDeseosByUsuario(Long idUsuario) {
        return Optional.empty();
    }

    @Override
    public ListaDeseos createListaDeseos(ListaDeseos listaDeseos) {
        return null;
    }

    @Override
    public ListaDeseos updateListaDeseos(ListaDeseos listaDeseos) {
        return null;
    }

    @Override
    public void deleteListaDeseos(Long id) {

    }
}
