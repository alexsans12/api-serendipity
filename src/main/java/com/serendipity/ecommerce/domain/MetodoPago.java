package com.serendipity.ecommerce.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.serendipity.ecommerce.converter.TipoPagoConverter;
import com.serendipity.ecommerce.enumeration.TipoPago;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
@Entity
@Table(name = "metodo_pago")
public class MetodoPago {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id_metodo_pago")
    private Integer idMetodoPago;

    @Convert(converter = TipoPagoConverter.class)
    @Column(nullable = false)
    private TipoPago tipo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String detalle;
}
