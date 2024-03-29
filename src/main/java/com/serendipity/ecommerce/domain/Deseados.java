package com.serendipity.ecommerce.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;
import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
@Entity
@Table(name = "lista_deseos")
public class Deseados {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id_lista_deseos")
    private Long idListaDeseos;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @JsonIgnore
    @OneToMany(mappedBy = "deseados", cascade = ALL, fetch = LAZY)
    private List<ProductoDeseado> productosDeseados;
}
