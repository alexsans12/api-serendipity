package com.serendipity.ecommerce.service.implementation;

import com.serendipity.ecommerce.domain.MetodoPago;
import com.serendipity.ecommerce.domain.Pago;
import com.serendipity.ecommerce.repository.MetodoPagoRepository;
import com.serendipity.ecommerce.service.MetodoPagoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MetodoPagoServiceImpl implements MetodoPagoService {
    private final MetodoPagoRepository metodoPagoRepository;

    @Override
    public Iterable<MetodoPago> getAllMetodoPagos() {
        return metodoPagoRepository.findAll();
    }

    @Override
    public Optional<MetodoPago> getMetodoPagoById(Long id) {
        return metodoPagoRepository.findById(id);
    }

    @Override
    public MetodoPago createMetodoPago(MetodoPago metodoPago) {
        return metodoPagoRepository.save(metodoPago);
    }

    @Override
    public MetodoPago updateMetodoPago(MetodoPago metodoPago) {
        return metodoPagoRepository.save(metodoPago);
    }

    @Override
    public void deleteMetodoPago(Long id) {
        metodoPagoRepository.deleteById(id);
    }
}
