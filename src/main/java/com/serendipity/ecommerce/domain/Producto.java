package com.serendipity.ecommerce.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.serendipity.ecommerce.dto.UsuarioDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;
import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.TemporalType.TIMESTAMP;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
@Entity
public class Producto {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id_producto")
    private Long idProducto;

    @Column(name = "id_marca", nullable = false)
    private Long idMarca;

    @Column(name = "id_categoria", nullable = false)
    private Long idCategoria;

    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;

    @Column(name = "sku", nullable = false, unique = true, length = 12, updatable = false)
    private String sku;

    @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "descuento", nullable = false, precision = 10, scale = 2)
    private BigDecimal descuento;

    @Column(name = "estado", nullable = false)
    private boolean estado;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    @Temporal(TIMESTAMP)
    private LocalDateTime fechaCreacion;

    @Column(name = "creado_por", nullable = false, updatable = false)
    private Long creadoPor;

    @Column(name = "fecha_modificacion")
    @Temporal(TIMESTAMP)
    private LocalDateTime fechaModificacion;

    @Column(name = "modificado_por")
    private Long modificadoPor;

    @ManyToOne(fetch = EAGER, cascade = ALL)
    @JoinColumn(name = "id_marca", referencedColumnName = "id_marca", insertable = false, updatable = false)
    private Marca marca;

    @ManyToOne(fetch = EAGER, cascade = ALL)
    @JoinColumn(name = "id_categoria", referencedColumnName = "id_categoria", insertable = false, updatable = false)
    private Categoria categoria;

    @ManyToOne(fetch = LAZY, cascade = MERGE)
    @JoinColumn(name = "creado_por", referencedColumnName = "id_usuario", insertable = false, updatable = false)
    private Usuario creadoPorUsuario;

    @ManyToOne(fetch = LAZY, cascade = MERGE)
    @JoinColumn(name = "modificado_por", referencedColumnName = "id_usuario", insertable = false, updatable = false)
    private Usuario modificadoPorUsuario;

    @OneToMany(mappedBy = "producto", fetch = EAGER)
    private List<ImagenProducto> imagenesProducto;
}
