package com.serendipity.ecommerce.domain;

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
@Table(name = "producto_lista", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_lista_deseos", "id_producto"})
})
public class ProductoDeseado {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id_producto_lista")
    private Long idProductoLista;

    @Column(name = "id_lista_deseos")
    private Long idListaDeseos;

    @Column(name = "id_producto", nullable = false)
    private Long idProducto;

    @ManyToOne
    @JoinColumn(name = "id_lista_deseos", referencedColumnName = "id_lista_deseos", insertable = false, updatable = false)
    private Deseados deseados;

    @ManyToOne
    @JoinColumn(name = "id_producto", referencedColumnName = "id_producto", insertable = false, updatable = false)
    private Producto producto;
}
