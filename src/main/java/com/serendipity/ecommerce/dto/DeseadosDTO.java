package com.serendipity.ecommerce.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DeseadosDTO {
    private Long idListaDeseos;
    private Long idUsuario;
    private List<ProductoDeseadoDTO> productosDeseados;
}
