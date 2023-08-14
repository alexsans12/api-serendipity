package com.serendipity.ecommerce.query;

public class RolQuery {
    public static final String INSERT_ROL_TO_USUARIO_QUERY = "INSERT INTO roles_usuario (id_usuario, id_rol) VALUES (:usuarioId, :rolId)";
    public static final String SELECT_ROL_BY_NAME_QUERY = "SELECT id_rol, nombre, permiso FROM rol WHERE nombre = :rolNombre";
}
