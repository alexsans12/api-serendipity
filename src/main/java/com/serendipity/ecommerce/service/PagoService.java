package com.serendipity.ecommerce.service;

import com.serendipity.ecommerce.domain.Pago;

import java.util.Optional;

public interface PagoService {
    Iterable<Pago> getAllPagos();

    Optional<Pago> getPagoById(Long id);

    Pago createPago(Pago pago);

    Pago updatePago(Pago pago);

    void deletePago(Long id);
}
