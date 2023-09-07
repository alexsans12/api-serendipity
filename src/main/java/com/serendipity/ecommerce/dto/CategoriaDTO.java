package com.serendipity.ecommerce.dto;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CategoriaDTO {
    private Long idCategoria;
    private Long idCategoriaPadre;
    private String nombre;
    private String descripcion;
    // Usuario que creo la categoria
    private Long creadoPor;
    private String nombreCreadoPor;
    private String apellidoCreadoPor;
    private LocalDateTime fechaCreacion;
    // Usuario que modifico la categoria
    private Long modificadoPor;
    private String nombreModificadoPor;
    private String apellidoModificadoPor;
    private LocalDateTime fechaModificacion;
    private CategoriaDTO categoriaPadre;
}
