package com.serendipity.ecommerce.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CarritoDTO {
    private Long idCarrito;
    private Long idUsuario;
    private BigDecimal total;
    private BigDecimal descuento;
    private List<CarritoProductoDTO> carritoProductos;
}
