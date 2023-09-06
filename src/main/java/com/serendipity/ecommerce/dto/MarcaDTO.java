package com.serendipity.ecommerce.dto;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MarcaDTO {
    private Long idMarca;
    private String nombre;
    private String urlImagen;
    private String descripcion;
    private boolean estado;
    // Usuario que creo la marca
    private Long creadoPor;
    private String nombreCreadoPor;
    private String apellidoCreadoPor;
    private LocalDateTime fechaCreacion;
    // Usuario que modifico la marca
    private Long modificadoPor;
    private String nombreModificadoPor;
    private String apellidoModificadoPor;
    private LocalDateTime fechaModificacion;
}
