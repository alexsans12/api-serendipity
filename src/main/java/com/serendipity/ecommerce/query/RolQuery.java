package com.serendipity.ecommerce.query;

public class RolQuery {
    public static final String SELECT_ROLES_QUERY = "SELECT id_rol, nombre, permiso FROM rol ORDER BY id_rol";
    public static final String SELECT_ROL_BY_NAME_QUERY = "SELECT id_rol, nombre, permiso FROM rol WHERE nombre = :nombre";
    public static final String SELECT_ROL_BY_ID_QUERY = "SELECT r.id_rol, r.nombre, r.permiso FROM rol r JOIN roles_usuario ru ON ru.id_rol = r.id_rol JOIN usuario u ON u.id_usuario = ru.id_usuario WHERE u.id_usuario = :id_usuario";
    public static final String INSERT_ROL_TO_USUARIO_QUERY = "INSERT INTO roles_usuario (id_usuario, id_rol) VALUES (:usuarioId, :rolId)";
    public static final String UPDATE_ROL_TO_USUARIO_QUERY = "UPDATE roles_usuario SET id_rol = :rolId WHERE id_usuario = :usuarioId";
}
