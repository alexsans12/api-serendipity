package com.serendipity.ecommerce.dto;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UsuarioDTO {
    private Long idUsuario;
    private String nombre;
    private String apellido;
    private String urlFoto;
    private String email;
    private String telefono;
    private Long creadoPor;
    private LocalDateTime fechaCreacion;
    private Long modificadoPor;
    private LocalDateTime fechaModificacion;
    private boolean utilizaMfa;
    private boolean estado;
    private String rol;
    private String permisos;
}
