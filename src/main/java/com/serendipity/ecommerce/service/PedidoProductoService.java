package com.serendipity.ecommerce.service;

import com.serendipity.ecommerce.domain.Pago;
import com.serendipity.ecommerce.domain.PedidoProducto;

import java.util.Optional;

public interface PedidoProductoService {
    Iterable<PedidoProducto> getAllPedidoProducto();

    Optional<PedidoProducto> getPedidoProductoById(Long id);

    PedidoProducto createPedidoProducto(PedidoProducto pedidoProducto);

    PedidoProducto updatePedidoProducto(PedidoProducto pedidoProducto);

    void deletePedidoProducto(Long id);
    void deleteAllByIdPedido(Long idPedido);
}
