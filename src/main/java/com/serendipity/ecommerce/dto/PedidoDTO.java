package com.serendipity.ecommerce.dto;

import com.serendipity.ecommerce.domain.Direccion;
import com.serendipity.ecommerce.domain.Pago;
import com.serendipity.ecommerce.enumeration.EstadoPedido;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PedidoDTO {
    private Long idPedido;
    private Long idUsuario;
    private Long idPago;
    private Long idDireccion;
    private Direccion direccion;
    private Pago pago;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
    private BigDecimal total;
    private String estado;
    private List<PedidoProductoDTO> productosPedido;
}
