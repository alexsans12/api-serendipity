package com.serendipity.ecommerce.service.implementation;

import com.serendipity.ecommerce.domain.Pedido;
import com.serendipity.ecommerce.repository.PedidoRepository;
import com.serendipity.ecommerce.service.PedidoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.data.domain.PageRequest.of;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PedidoServiceImpl implements PedidoService {
    private final PedidoRepository pedidoRepository;

    @Override
    public Page<Pedido> getAllPedidos(int page, int size) {
        return pedidoRepository.findAll(of(page, size));
    }

    @Override
    public Page<Pedido> getPedidosByUsuario(Long idUsuario, int page, int size) {
        return pedidoRepository.findPedidosByIdUsuario(idUsuario, of(page, size));
    }

    @Override
    public Optional<Pedido> getPedidoById(Long id) {
        return pedidoRepository.findById(id);
    }

    @Override
    public Pedido createPedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido updatePedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Override
    public void deletePedido(Long id) {
        pedidoRepository.deleteById(id);
    }
}
