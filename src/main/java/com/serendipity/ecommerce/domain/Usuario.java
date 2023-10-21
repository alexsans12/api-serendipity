package com.serendipity.ecommerce.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_NULL)
@Entity
public class Usuario {
    @Id
    @Column(name = "id_usuario")
    private Long idUsuario;
    @NotEmpty(message = "El nombre no puede estar vacío")
    private String nombre;
    @NotEmpty(message = "El apellido no puede estar vacío")
    private String apellido;
    private String urlFoto;
    @NotEmpty(message = "El email no puede estar vacío")
    @Email(message = "Correo electrónico no válido. Por favor, ingrese un correo electrónico válido")
    private String email;
    @NotEmpty(message = "La contraseña no puede estar vacía")
    private String password;
    private String telefono;
    private Long creadoPor;
    private LocalDateTime fechaCreacion;
    private Long modificadoPor;
    private LocalDateTime fechaModificacion;
    private boolean utilizaMfa;
    private boolean estado;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "roles_usuario",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_rol")
    )
    private Set<Rol> roles;
}
