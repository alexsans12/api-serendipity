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
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
@Entity
public class ImagenProducto {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id_imagen_producto")
    private Long idImagenProducto;

    @Column(name = "id_producto", nullable = false)
    private Long idProducto;

    @Column(name = "url", nullable = false, length = 255)
    private String url;

    @JsonIgnore
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "id_producto", referencedColumnName = "id_producto", insertable = false, updatable = false)
    private Producto producto;

}
