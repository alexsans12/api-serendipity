package com.serendipity.ecommerce.form;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressForm {
    private Long idDireccion;
    @NotEmpty(message = "El nombre no puede estar vacío")
    @Size(max = 50, message = "El nombre debe tener máximo 50 caracteres")
    private String nombre;
    @NotEmpty(message = "El apellido no puede estar vacío")
    @Size(max = 50, message = "El apellido debe tener máximo 50 caracteres")
    private String apellido;
    @NotEmpty(message = "El teléfono no puede estar vacío")
    @Pattern(regexp = "[0-9]{8}", message = "El teléfono debe tener 8 dígitos")
    private String telefono;
    @NotNull(message = "Debe seleccionar un departamento")
    private Long idDepartamento;
    @NotNull(message = "Debe seleccionar un municipio")
    private Long idMunicipio;
    @NotEmpty(message = "La dirección no puede estar vacía")
    @Size(max = 255, message = "La dirección debe tener máximo 255 caracteres")
    private String direccion;
    private String indicaciones;
}
