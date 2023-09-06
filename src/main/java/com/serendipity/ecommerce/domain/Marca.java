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
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_DEFAULT)
@Entity
@Table(name = "marca")
public class Marca {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id_marca")
    private Long idMarca;

    @Column(name = "nombre", nullable = false, length = 50, unique = true)
    private String nombre;

    @Column(name = "url_imagen", nullable = false, length = 255)
    private String urlImagen;

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

    @Column(name = "estado")
    private boolean estado;

    @ManyToOne(fetch = EAGER, cascade = ALL)
    @JoinColumn(name = "creado_por", referencedColumnName = "id_usuario", insertable = false, updatable = false)
    private Usuario creadoPorUsuario;

    @ManyToOne(fetch = EAGER, cascade = ALL)
    @JoinColumn(name = "modificado_por", referencedColumnName = "id_usuario", insertable = false, updatable = false)
    private Usuario modificadoPorUsuario;
}
