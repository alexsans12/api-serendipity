package com.serendipity.ecommerce.repository;

import com.serendipity.ecommerce.domain.PedidoProducto;
import org.springframework.data.repository.CrudRepository;

public interface PedidoProductoRepository extends CrudRepository<PedidoProducto, Long> {
    void deleteAllByIdPedido(Long idPedido);
}
