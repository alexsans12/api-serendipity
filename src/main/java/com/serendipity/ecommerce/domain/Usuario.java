package com.serendipity.ecommerce.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class Usuario {
    private Long idUsuario;
    @NotEmpty(message = "El nombre no puede ser vacío")
    private String nombre;
    private String urlFoto;
    @NotEmpty(message = "El email no puede estar vacío")
    @Email(message = "Correo electrónico no válido. Por favor, ingrese un correo electrónico válido")
    private String email;
    @NotEmpty(message = "La contraseña no puede estar vacía")
    private String password;
    private Long creadoPor;
    private String fechaCreacion;
    private Long modificadoPor;
    private String fechaModificacion;
    private Boolean utilizaMfa;
    private Boolean estado;
}
