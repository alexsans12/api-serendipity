package com.serendipity.ecommerce.dto;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Long idUsuario;
    private String nombre;
    private String urlFoto;
    private String email;
    private Long creadoPor;
    private String fechaCreacion;
    private Long modificadoPor;
    private String fechaModificacion;
    private Boolean utilizaMfa;
    private Boolean estado;
}
