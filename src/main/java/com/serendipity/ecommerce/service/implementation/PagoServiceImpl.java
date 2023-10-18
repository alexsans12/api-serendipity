package com.serendipity.ecommerce.service.implementation;

import com.serendipity.ecommerce.domain.Pago;
import com.serendipity.ecommerce.repository.PagoRepository;
import com.serendipity.ecommerce.service.PagoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PagoServiceImpl implements PagoService {
    private final PagoRepository pagoRepository;

    @Override
    public Iterable<Pago> getAllPagos() {
        return pagoRepository.findAll();
    }

    @Override
    public Optional<Pago> getPagoById(Long id) {
        return pagoRepository.findById(id);
    }

    @Override
    public Pago createPago(Pago pago) {
        return pagoRepository.save(pago);
    }

    @Override
    public Pago updatePago(Pago pago) {
        return pagoRepository.save(pago);
    }

    @Override
    public void deletePago(Long id) {
        pagoRepository.deleteById(id);
    }
}
