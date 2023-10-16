package com.serendipity.ecommerce.repository;

import com.serendipity.ecommerce.domain.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PedidoRepository extends PagingAndSortingRepository<Pedido, Long>, ListCrudRepository<Pedido, Long> {
    Page<Pedido> findPedidosByIdUsuario(Long idUsuario, Pageable pageable);
}
