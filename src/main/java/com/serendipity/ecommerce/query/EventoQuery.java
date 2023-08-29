package com.serendipity.ecommerce.query;

public class EventoQuery {
    public static final String SELECT_EVENTOS_BY_USUARIO_ID_QUERY = "SELECT evu.id_eventos_usuario, evu.dispositivo, evu.direccion_ip, ev.tipo, ev.descripcion, evu.fecha_creacion FROM evento as ev " +
                                                                "INNER JOIN eventos_usuario as evu ON ev.id_evento = evu.id_evento " +
                                                                "INNER JOIN usuario u ON u.id_usuario = evu.id_usuario " +
                                                                "WHERE u.id_usuario = :usuarioId ORDER BY evu.fecha_creacion DESC LIMIT 10";
    public static final String INSERT_EVENTO_BY_USUARIO_EMAIL_QUERY = "INSERT INTO eventos_usuario (id_usuario, id_evento, dispositivo, direccion_ip) " +
                                                                "VALUES ((SELECT id_usuario FROM usuario WHERE email = :email), (SELECT id_evento FROM evento WHERE tipo = :eventoType), :dispositivo, :ipAddress)";
    public static final String INSERT_EVENTO_BY_USUARIO_ID_QUERY = "INSERT INTO eventos_usuario (id_usuario, id_evento, dispositivo, direccion_ip) " +
                                                                "VALUES (:id_usuario, (SELECT id_evento FROM evento WHERE tipo = :eventoType), :dispositivo, :ipAddress)";
}
