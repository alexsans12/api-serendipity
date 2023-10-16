package com.serendipity.ecommerce.service;

import com.serendipity.ecommerce.domain.MetodoPago;

import java.util.Optional;

public interface MetodoPagoService {
    Iterable<MetodoPago> getAllMetodoPagos();

    Optional<MetodoPago> getMetodoPagoById(Long id);

    MetodoPago createMetodoPago(MetodoPago metodoPago);

    MetodoPago updateMetodoPago(MetodoPago metodoPago);

    void deleteMetodoPago(Long id);
}
