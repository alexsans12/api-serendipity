package com.serendipity.ecommerce.dto;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PedidoProductoDTO {
    private Long idPedidoProducto;
    private Long idPedido;
    private Long idProducto;
    private Integer cantidad;
    private BigDecimal precio;
    private BigDecimal subtotal;
    private BigDecimal descuento;
    private ProductoDTO producto;
}
