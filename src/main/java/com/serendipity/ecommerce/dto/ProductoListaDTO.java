package com.serendipity.ecommerce.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductoListaDTO {
    private Long idProductoLista;
    private Long idListaDeseos;
    private Long idProducto;
    private ProductoDTO producto;
}
