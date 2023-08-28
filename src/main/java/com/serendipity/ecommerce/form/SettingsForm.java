package com.serendipity.ecommerce.form;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SettingsForm {
    @NotNull(message = "Atributo 'enabled' no puede estar vacío o ser nulo")
    private Boolean enabled;
}
