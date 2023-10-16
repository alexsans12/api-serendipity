package com.serendipity.ecommerce.dtomapper;

import com.serendipity.ecommerce.domain.PedidoProducto;
import com.serendipity.ecommerce.dto.PedidoProductoDTO;

import static com.serendipity.ecommerce.dtomapper.ProductoDTOMapper.fromProducto;
import static org.springframework.beans.BeanUtils.copyProperties;

public class PedidoProductoDTOMapper {
    public static PedidoProductoDTO fromPedidoProducto(PedidoProducto pedidoProducto) {
        PedidoProductoDTO pedidoProductoDTO = new PedidoProductoDTO();
        copyProperties(pedidoProducto, pedidoProductoDTO);
        pedidoProductoDTO.setProducto(fromProducto(pedidoProducto.getProducto()));
        return pedidoProductoDTO;
    }
}
