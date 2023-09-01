package com.serendipity.ecommerce.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewPasswordForm {
    @NotNull(message = "Id de usuario no puede ser nulo")
    private Long idUsuario;
    @NotEmpty(message = "Contraseña no puede estar vacía")
    private String password;
    @NotEmpty(message = "La confirmación de contraseña no puede estar vacía")
    private String confirmPassword;
}
