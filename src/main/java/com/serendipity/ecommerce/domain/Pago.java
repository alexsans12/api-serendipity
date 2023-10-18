package com.serendipity.ecommerce.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.serendipity.ecommerce.converter.EstadoPagoConverter;
import com.serendipity.ecommerce.converter.TipoPagoConverter;
import com.serendipity.ecommerce.enumeration.EstadoPago;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
@Entity
@Table(name = "pago")
public class Pago {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id_pago")
    private Long idPago;

    @Column(name = "id_metodo_pago", nullable = false)
    private Long idMetodoPago;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "id_metodo_pago", referencedColumnName = "id_metodo_pago", insertable = false, updatable = false)
    private MetodoPago metodoPago;

    @Column(nullable = false)
    private BigDecimal monto;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Column(name = "fecha_creacion", nullable = false, updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaCreacion;
}
