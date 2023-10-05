package com.serendipity.ecommerce.service.implementation;

import com.serendipity.ecommerce.domain.ProductoDeseado;
import com.serendipity.ecommerce.repository.ProductoDeseadoRepository;
import com.serendipity.ecommerce.service.ProductoDeseadoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProductoDeseadoServiceImpl implements ProductoDeseadoService {
    private final ProductoDeseadoRepository productoDeseadoRepository;

    @Override
    public Iterable<ProductoDeseado> getAllProductoDeseado() {
        return productoDeseadoRepository.findAll();
    }

    @Override
    public Iterable<ProductoDeseado> findAllProductoDeseadoByIdDeseados(Long idListaDeseos) {
        return productoDeseadoRepository.findAllProductosDeseadoByIdListaDeseos(idListaDeseos);
    }

    @Override
    public Optional<ProductoDeseado> getProductoDeseadoById(Long id) {
        return productoDeseadoRepository.findById(id);
    }

    @Override
    public ProductoDeseado createProductoDeseado(ProductoDeseado productoDeseado) {
        return productoDeseadoRepository.save(productoDeseado);
    }

    @Override
    public ProductoDeseado updateProductoDeseado(ProductoDeseado productoDeseado) {
        return productoDeseadoRepository.save(productoDeseado);
    }

    @Override
    public void deleteProductoDeseado(Long id) {
        productoDeseadoRepository.deleteById(id);
    }

    @Override
    public Optional<ProductoDeseado> findProductoDeseadoByIdDeseadosAndIdProducto(Long idListaDeseos, Long idProducto) {
        return productoDeseadoRepository.findByIdListaDeseosAndIdProducto(idListaDeseos, idProducto);
    }
}
