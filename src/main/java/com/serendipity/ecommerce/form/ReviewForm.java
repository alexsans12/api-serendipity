package com.serendipity.ecommerce.form;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewForm {
    private Long idReview;

    @NotNull(message = "El id del producto no puede ser nulo")
    private Long idProducto;

    @NotEmpty(message = "El mensaje no puede ser vacío")
    private String comentario;

    @NotNull(message = "La puntuación no puede ser nula")
    @Min(value = 1, message = "La puntuación debe ser al menos 1")
    @Max(value = 5, message = "La puntuación debe ser como máximo 5")
    private Integer puntuacion;
}
