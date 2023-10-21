package com.serendipity.ecommerce.rowmapper;

import com.serendipity.ecommerce.domain.Stats;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StatsRowMapper implements RowMapper<Stats> {

    @Override
    public Stats mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Stats.builder()
                .totalUsuarios(rs.getInt("total_usuarios"))
                .totalProductos(rs.getInt("total_productos"))
                .totalPedidos(rs.getInt("total_pedidos"))
                .totalBilled(rs.getBigDecimal("total_billed"))
                .build();
    }
}
