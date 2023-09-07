package com.serendipity.ecommerce.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.serendipity.ecommerce.dto.UsuarioDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;
import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
@Entity
@Table(name = "categoria")
public class Categoria {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id_categoria")
    private Long idCategoria;

    @Column(name = "id_categoria_padre")
    private Long idCategoriaPadre;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaCreacion;

    @Column(name = "creado_por", nullable = false, updatable = false)
    private Long creadoPor;

    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaModificacion;

    @Column(name = "modificado_por")
    private Long modificadoPor;

    @ManyToOne(fetch = EAGER, cascade = ALL)
    @JoinColumn(name = "creado_por", referencedColumnName = "id_usuario", insertable = false, updatable = false)
    private Usuario creadoPorUsuario;

    @ManyToOne(fetch = EAGER, cascade = ALL)
    @JoinColumn(name = "modificado_por", referencedColumnName = "id_usuario", insertable = false, updatable = false)
    private Usuario modificadoPorUsuario;

    @ManyToOne(fetch = EAGER, cascade = ALL)
    @JoinColumn(name = "id_categoria_padre", referencedColumnName = "id_categoria", insertable = false, updatable = false)
    private Categoria categoriaPadre;
}
