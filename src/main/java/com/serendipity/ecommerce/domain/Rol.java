package com.serendipity.ecommerce.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_NULL)
@Entity
@Table(name = "rol")
public class Rol {
    @Id
    @Column(name = "id_rol")
    private Long idRol;
    private String nombre;
    private String permiso;

    @ManyToMany(mappedBy = "roles")
    private Set<Usuario> usuarios;
}
