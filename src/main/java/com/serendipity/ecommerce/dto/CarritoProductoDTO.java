package com.serendipity.ecommerce.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CarritoProductoDTO {
    private Long idCarritoProducto;
    private Long idCarrito;
    private Long idProducto;
    private Integer cantidad;
    private ProductoDTO producto;
}
