package com.serendipity.ecommerce.service;

import com.serendipity.ecommerce.domain.Pedido;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface PedidoService {
    Page<Pedido> getAllPedidos(int page, int size);
    Page<Pedido> getPedidosByUsuario(Long idUsuario, int page, int size);
    Optional<Pedido> getPedidoById(Long id);
    Pedido createPedido(Pedido pedido);
    Pedido updatePedido(Pedido pedido);
    void deletePedido(Long id);
}
