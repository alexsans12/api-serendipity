package com.serendipity.ecommerce.enumeration;

public enum EventoType {
    INTENTO_LOGIN("Intentaste iniciar sesión"),
    FALLO_LOGIN("Falló el inicio de sesión"),
    EXITO_LOGIN("Iniciaste sesión correctamente"),
    ACTUALIZACION_PERFIL("Actualizaste tu perfil"),
    ACTUALIZACION_FOTO_PERFIL("Actualizaste tu foto de perfil"),
    ACTUALIZACION_ROL("Actualizaste tu rol"),
    ACTUALIZACION_CONFIGURACION_CUENTA("Actualizaste tu configuración de cuenta"),
    ACTUALIZACION_CONTRASENA("Actualizaste tu contraseña"),
    ACTUALIZACION_MFA("Actualizaste tu configuración de MFA");

    private final String descripcion;

    EventoType(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
