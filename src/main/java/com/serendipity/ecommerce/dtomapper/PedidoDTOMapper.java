package com.serendipity.ecommerce.dtomapper;

import com.serendipity.ecommerce.domain.Pedido;
import com.serendipity.ecommerce.domain.PedidoProducto;
import com.serendipity.ecommerce.dto.PedidoDTO;
import com.serendipity.ecommerce.dto.PedidoProductoDTO;

import java.util.ArrayList;
import java.util.List;

import static com.serendipity.ecommerce.dtomapper.PedidoProductoDTOMapper.fromPedidoProducto;
import static org.springframework.beans.BeanUtils.copyProperties;

public class PedidoDTOMapper {
    public static PedidoDTO fromPedido(Pedido pedido, List<PedidoProducto> pedidoProductos) {
        PedidoDTO pedidoDTO = new PedidoDTO();
        copyProperties(pedido, pedidoDTO);
        List<PedidoProductoDTO> pedidoProductoDTOList = new ArrayList<>();

        for (PedidoProducto pedidoProducto : pedidoProductos) {
            pedidoProductoDTOList.add(fromPedidoProducto(pedidoProducto));
        }
        pedidoDTO.setProductosPedido(pedidoProductoDTOList);

        return pedidoDTO;
    }

    public static PedidoDTO fromPedido(Pedido pedido) {
        PedidoDTO pedidoDTO = new PedidoDTO();
        copyProperties(pedido, pedidoDTO);
        List<PedidoProductoDTO> pedidoProductoDTOList = new ArrayList<>();

        for (PedidoProducto pedidoProducto : pedido.getPedidoProductos()) {
            pedidoProductoDTOList.add(fromPedidoProducto(pedidoProducto));
        }
        pedidoDTO.setProductosPedido(pedidoProductoDTOList);

        return pedidoDTO;
    }
}
