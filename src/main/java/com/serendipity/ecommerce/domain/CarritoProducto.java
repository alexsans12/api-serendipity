package com.serendipity.ecommerce.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
@Entity
@Table(name = "carrito_producto")
public class CarritoProducto {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id_carrito_producto")
    private Long idCarritoProducto;

    @Column(name = "id_carrito")
    private Long idCarrito;

    @Column(name = "id_producto", nullable = false)
    private Long idProducto;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "id_carrito", referencedColumnName = "id_carrito", insertable = false, updatable = false)
    private Carrito carrito;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "id_producto", referencedColumnName = "id_producto", insertable = false, updatable = false)
    private Producto producto;
}
