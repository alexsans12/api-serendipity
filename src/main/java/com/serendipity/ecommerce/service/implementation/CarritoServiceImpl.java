package com.serendipity.ecommerce.service.implementation;

import com.serendipity.ecommerce.domain.Carrito;
import com.serendipity.ecommerce.repository.CarritoRepository;
import com.serendipity.ecommerce.service.CarritoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CarritoServiceImpl implements CarritoService {
    private final CarritoRepository carritoRepository;

    @Override
    public Iterable<Carrito> getAllCarritos() {
        return carritoRepository.findAll();
    }

    @Override
    public Optional<Carrito> getCarritoById(Long id) {
        return carritoRepository.findById(id);
    }

    @Override
    public Optional<Carrito> getCarritoByUsuario(Long idUsuario) {
        return carritoRepository.findByIdUsuario(idUsuario);
    }

    @Override
    public Carrito createCarrito(Carrito carrito) {
        return carritoRepository.save(carrito);
    }

    @Override
    public Carrito updateCarrito(Carrito carrito) {
        return carritoRepository.save(carrito);
    }

    @Override
    public void deleteCarrito(Long id) {
        carritoRepository.deleteById(id);
    }
}
