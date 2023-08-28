package com.serendipity.ecommerce.query;

public class UsuarioQuery {

    public static final String SELECT_USUARIO_BY_EMAIL_QUERY = "SELECT * FROM usuario WHERE email = :email";
    public static final String SELECT_USUARIO_BY_USUARIO_CODE_QUERY = "SELECT * FROM usuario WHERE id_usuario = (SELECT id_usuario FROM verificacion_two_factors WHERE codigo = :code)";
    public static final String SELECT_EXPIRATION_BY_URL_QUERY = "SELECT fecha_expiracion < NOW() AS is_expired FROM restablecer_password WHERE url = :url";
    public static final String SELECT_USUARIO_BY_PASSWORD_URL_QUERY = "SELECT * FROM usuario WHERE id_usuario = (SELECT id_usuario FROM restablecer_password WHERE url = :url)";
    public static final String SELECT_CODE_EXPIRATION_QUERY = "SELECT fecha_expiracion < NOW() AS is_expired FROM verificacion_two_factors WHERE codigo = :code";
    public static final String SELECT_USUARIO_BY_ACCOUNT_URL_QUERY = "SELECT * FROM usuario WHERE id_usuario = (SELECT id_usuario FROM verificacion_cuenta WHERE url = :url)";
    public static final String SELECT_USUARIO_BY_ID_QUERY = "SELECT * FROM usuario WHERE id_usuario = :id";
    public static final String COUNT_USUARIO_EMAIL_QUERY = "SELECT COUNT(*) FROM usuario WHERE email = :email";
    public static final String INSERT_USUARIO_QUERY = "INSERT INTO usuario (nombre, apellido, email, password, utiliza_mfa, estado) VALUES (:nombre, :apellido, :email, :password, :utiliza_mfa, :estado)";
    public static final String INSERT_VERIFICACION_CUENTA_URL_QUERY = "INSERT INTO verificacion_cuenta (id_usuario, url, fecha_expiracion) VALUES (:idUsuario, :url, :fecha)";
    public static final String INSERT_VERIFICATION_CODE_QUERY = "INSERT INTO verificacion_two_factors (id_usuario, codigo, fecha_expiracion) VALUES (:usuario_id, :codigo, :fecha_expiracion)";
    public static final String INSERT_PASSWORD_VERIFICATION_QUERY = "INSERT INTO restablecer_password (id_usuario, url, fecha_expiracion) VALUES (:id_usuario, :url, :fecha_expiracion)";
    public static final String UPDATE_USUARIO_PASSWORD_BY_URL_QUERY = "UPDATE usuario SET password = :password WHERE id_usuario = (SELECT id_usuario FROM restablecer_password WHERE url = :url)";
    public static final String UPDATE_USUARIO_ENABLED_QUERY = "UPDATE usuario SET estado = :enabled WHERE id_usuario = :id_usuario";
    public static final String UPDATE_USUARIO_DETAILS_QUERY = "UPDATE usuario SET nombre = :nombre, apellido = :apellido, email = :email, telefono = :telefono, modificado_por = :modificado_por WHERE id_usuario = :id_usuario";
    public static final String UPDATE_USUARIO_PASSWORD_BY_ID_QUERY = "UPDATE usuario SET password = :password WHERE id_usuario = :id_usuario";
    public static final String UPDATE_USUARIO_MFA_QUERY = "UPDATE usuario SET utiliza_mfa = :utiliza_mfa WHERE email = :email";
    public static final String DELETE_VERIFICATION_BY_URL_QUERY = "DELETE FROM restablecer_password WHERE url = :url";
    public static final String DELETE_VERIFICATION_BY_USUARIO_ID = "DELETE FROM verificacion_two_factors WHERE id_usuario = :id";
    public static final String DELETE_CODE = "DELETE FROM verificacion_two_factors WHERE codigo = :code";
    public static final String DELETE_PASSWORD_VERIFICATION_BY_USUARIO_ID_QUERY = "DELETE FROM restablecer_password WHERE id_usuario = :id";
}
