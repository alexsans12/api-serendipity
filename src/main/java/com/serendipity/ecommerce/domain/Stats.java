package com.serendipity.ecommerce.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class Stats {
    private int totalUsuarios;
    private int totalPedidos;
    private int totalProductos;
    private BigDecimal totalBilled;
}
