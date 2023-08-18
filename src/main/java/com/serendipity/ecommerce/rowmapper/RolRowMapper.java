package com.serendipity.ecommerce.rowmapper;

import com.serendipity.ecommerce.domain.Rol;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RolRowMapper implements RowMapper<Rol> {
    @Override
    public Rol mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Rol.builder()
                .idRol(resultSet.getLong("id_rol"))
                .nombre(resultSet.getString("nombre"))
                .permiso(resultSet.getString("permiso"))
                .build();
    }
}
