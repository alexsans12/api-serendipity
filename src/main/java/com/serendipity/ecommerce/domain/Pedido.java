package com.serendipity.ecommerce.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.serendipity.ecommerce.enumeration.EstadoPedido;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;
import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
@Entity
@Table(name = "pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id_pedido")
    private Long idPedido;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @JsonIgnore
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", insertable = false, updatable = false)
    private Usuario usuario;

    @Column(name = "id_pago", nullable = false)
    private Long idPago;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "id_pago", referencedColumnName = "id_pago", insertable = false, updatable = false)
    private Pago pago;

    @Column(name = "id_direccion", nullable = false)
    private Long idDireccion;

    @JsonIgnore
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "id_direccion", referencedColumnName = "id_direccion", insertable = false, updatable = false)
    private Direccion direccion;

    @Column(name = "fecha_creacion", nullable = false, updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion", columnDefinition = "ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime fechaModificacion;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(nullable = false)
    private String estado;

    @JsonIgnore
    @OneToMany(mappedBy = "pedido", fetch = EAGER)
    private List<PedidoProducto> pedidoProductos;
}
