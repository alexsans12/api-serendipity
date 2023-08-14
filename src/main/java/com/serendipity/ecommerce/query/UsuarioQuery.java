package com.serendipity.ecommerce.query;

public class UsuarioQuery {

    public static final String INSERT_USUARIO_QUERY = "INSERT INTO usuario (nombre, email, password) VALUES (:nombre, :email, :password)";
    public static final String COUNT_USUARIO_EMAIL_QUERY = "SELECT COUNT(*) FROM usuario WHERE email = :email";
    public static final String INSERT_VERIFICACION_CUENTA_URL_QUERY = "INSERT INTO verificacion_cuenta (id_usuario, url) VALUES (:idUsuario, :url)";
}
