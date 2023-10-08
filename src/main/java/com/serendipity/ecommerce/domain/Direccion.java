package com.serendipity.ecommerce.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@Table(name = "direccion")
public class Direccion {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id_direccion")
    private Long idDireccion;

    @ManyToOne
    @JoinColumn(name = "id_municipio", nullable = false)
    private Municipio municipio;

    @JoinColumn(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 50)
    private String apellido;

    @Column(name = "telefono", nullable = false, length = 20)
    private String telefono;

    @Column(name = "direccion", nullable = false, length = 255)
    private String direccion;

    @Column(name = "indicaciones")
    private String indicaciones;

}
