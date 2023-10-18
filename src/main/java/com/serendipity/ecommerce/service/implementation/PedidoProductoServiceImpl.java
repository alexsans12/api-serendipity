package com.serendipity.ecommerce.service.implementation;

import com.serendipity.ecommerce.domain.PedidoProducto;
import com.serendipity.ecommerce.repository.PedidoProductoRepository;
import com.serendipity.ecommerce.service.PedidoProductoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PedidoProductoServiceImpl implements PedidoProductoService {
    private final PedidoProductoRepository pedidoProductoRepository;

    @Override
    public Iterable<PedidoProducto> getAllPedidoProducto() {
        return pedidoProductoRepository.findAll();
    }

    @Override
    public Optional<PedidoProducto> getPedidoProductoById(Long id) {
        return pedidoProductoRepository.findById(id);
    }

    @Override
    public PedidoProducto createPedidoProducto(PedidoProducto pedidoProducto) {
        return pedidoProductoRepository.save(pedidoProducto);
    }

    @Override
    public PedidoProducto updatePedidoProducto(PedidoProducto pedidoProducto) {
        return pedidoProductoRepository.save(pedidoProducto);
    }

    @Override
    public void deletePedidoProducto(Long id) {
        pedidoProductoRepository.deleteById(id);
    }

    @Override
    public void deleteAllByIdPedido(Long idPedido) {
        pedidoProductoRepository.deleteAllByIdPedido(idPedido);
    }
}
