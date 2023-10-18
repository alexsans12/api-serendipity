package com.serendipity.ecommerce.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordForm {
    @NotEmpty(message = "La contraseña actual no puede estar vacía")
    private String currentPassword;
    @NotEmpty(message = "La nueva contraseña no puede estar vacía")
    private String newPassword;
    @NotEmpty(message = "La confirmación de la nueva contraseña no puede estar vacía")
    private String confirmNewPassword;
}
