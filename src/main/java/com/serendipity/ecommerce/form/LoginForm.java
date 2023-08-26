package com.serendipity.ecommerce.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginForm {
    @NotEmpty(message = "El email no puede estar vacío")
    @Email(message = "Correo electrónico no válido. Por favor, ingrese un correo electrónico válido")
    private String email;
    @NotEmpty(message = "La contraseña no puede estar vacía")
    private String password;
}
