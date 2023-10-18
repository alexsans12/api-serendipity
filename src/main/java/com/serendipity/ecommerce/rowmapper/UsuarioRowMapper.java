package com.serendipity.ecommerce.rowmapper;

import com.serendipity.ecommerce.domain.Usuario;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioRowMapper implements RowMapper<Usuario> {
    @Override
    public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Usuario.builder()
                .idUsuario(rs.getLong("id_usuario"))
                .nombre(rs.getString("nombre"))
                .apellido(rs.getString("apellido"))
                .urlFoto(rs.getString("url_foto"))
                .telefono(rs.getString("telefono"))
                .email(rs.getString("email"))
                .password(rs.getString("password"))
                .creadoPor(rs.getLong("creado_por"))
                .fechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime())
                .modificadoPor(rs.getLong("modificado_por"))
                .fechaModificacion(rs.getTimestamp("fecha_modificacion").toLocalDateTime())
                .utilizaMfa(rs.getBoolean("utiliza_mfa"))
                .estado(rs.getBoolean("estado"))
                .build();
    }
}
