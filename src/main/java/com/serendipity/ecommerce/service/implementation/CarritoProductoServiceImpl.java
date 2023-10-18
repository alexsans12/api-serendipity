package com.serendipity.ecommerce.service.implementation;

import com.serendipity.ecommerce.domain.CarritoProducto;
import com.serendipity.ecommerce.domain.Producto;
import com.serendipity.ecommerce.exception.ApiException;
import com.serendipity.ecommerce.repository.CarritoProductoRepository;
import com.serendipity.ecommerce.service.CarritoProductoService;
import com.serendipity.ecommerce.service.ProductoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CarritoProductoServiceImpl implements CarritoProductoService {
    private final CarritoProductoRepository carritoProductoRepository;
    private final ProductoService productoService;

    @Override
    public Iterable<CarritoProducto> getAllCarritoProductos() {
        return carritoProductoRepository.findAll();
    }

    @Override
    public Iterable<CarritoProducto> findAllCarritoProductosByIdCarrito(Long idCarrito) {
        return carritoProductoRepository.findAllCarritoProductosByIdCarrito(idCarrito);
    }

    @Override
    public Optional<CarritoProducto> getCarritoProductoById(Long id) {
        return carritoProductoRepository.findById(id);
    }

    @Override
    public CarritoProducto createCarritoProducto(CarritoProducto carritoProducto) {
        Producto producto = productoService.getProductoById(carritoProducto.getIdProducto());
        if(producto.getCantidad() < carritoProducto.getCantidad()) {
            log.error("No hay suficientes productos en stock");
            throw new ApiException("No hay suficientes productos en stock");
        }
        return carritoProductoRepository.save(carritoProducto);
    }

    @Override
    public CarritoProducto updateCarritoProducto(CarritoProducto carritoProducto) {
        Producto producto = productoService.getProductoById(carritoProducto.getIdProducto());
        if(producto.getCantidad() < carritoProducto.getCantidad()) {
            log.error("No hay suficientes productos en stock");
            throw new ApiException("No hay suficientes productos en stock");
        }
        return carritoProductoRepository.save(carritoProducto);
    }

    @Override
    public void deleteCarritoProducto(Long id) {
        carritoProductoRepository.deleteByIdCarritoProducto(id);
    }

    @Override
    public Optional<CarritoProducto> findCarritoProductoByIdCarritoAndIdProducto(Long idCarrito, Long idProducto) {
        return carritoProductoRepository.findByIdCarritoAndIdProducto(idCarrito, idProducto);
    }
}
