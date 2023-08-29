package com.serendipity.ecommerce.rowmapper;

import com.serendipity.ecommerce.domain.UsuarioEvento;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioEventoRowMapper implements RowMapper<UsuarioEvento> {
    @Override
    public UsuarioEvento mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return UsuarioEvento.builder()
                .idEvento(resultSet.getLong("id_eventos_usuario"))
                .tipo(resultSet.getString("tipo"))
                .descripcion(resultSet.getString("descripcion"))
                .dispositivo(resultSet.getString("dispositivo"))
                .ipAddress(resultSet.getString("direccion_ip"))
                .fechaCreacion(resultSet.getTimestamp("fecha_creacion").toLocalDateTime())
                .build();
    }
}
