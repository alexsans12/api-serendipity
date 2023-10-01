package com.serendipity.ecommerce.service;

import com.serendipity.ecommerce.domain.Carrito;

import java.util.Optional;

public interface CarritoService {
    Iterable<Carrito> getAllCarritos();

    Optional<Carrito> getCarritoById(Long id);
    Optional<Carrito> getCarritoByUsuario(Long idUsuario);

    Carrito createCarrito(Carrito carrito);

    Carrito updateCarrito(Carrito carrito);

    void deleteCarrito(Long id);
}
