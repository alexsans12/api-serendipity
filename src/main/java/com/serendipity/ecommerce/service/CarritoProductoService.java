package com.serendipity.ecommerce.service;

import com.serendipity.ecommerce.domain.CarritoProducto;

import java.util.Optional;

public interface CarritoProductoService {
    Iterable<CarritoProducto> getAllCarritoProductos();
    Iterable<CarritoProducto> findAllCarritoProductosByIdCarrito(Long idCarrito);
    Optional<CarritoProducto> getCarritoProductoById(Long id);
    CarritoProducto createCarritoProducto(CarritoProducto carritoProducto);
    CarritoProducto updateCarritoProducto(CarritoProducto carritoProducto);
    void deleteCarritoProducto(Long id);
    Optional<CarritoProducto> findCarritoProductoByIdCarritoAndIdProducto(Long idCarrito, Long idProducto);
}
