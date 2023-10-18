package com.serendipity.ecommerce.form;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateProfileForm {
    @NotNull(message = "El idUsuario no puede estar vacío o ser nulo")
    private Long idUsuario;
    @NotEmpty(message = "El nombre no puede estar vacío")
    @Size(max=255, message = "El nombre no puede tener más de 255 caracteres")
    private String nombre;
    @NotEmpty(message = "El apellido no puede estar vacío")
    @Size(max=255, message = "El apellido no puede tener más de 255 caracteres")
    private String apellido;
    @NotEmpty(message = "El email no puede estar vacío")
    @Email(message = "Correo electrónico no válido. Por favor, ingrese un correo electrónico válido")
    @Pattern(regexp = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}", message = "Correo electrónico no válido. Por favor, ingrese un correo electrónico válido")
    private String email;
    @Pattern(regexp = "[0-9]{8}", message = "El teléfono debe tener 8 dígitos")
    private String telefono;
    private Long modificadoPor;
}
