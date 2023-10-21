package com.serendipity.ecommerce.query;

public class ClientQuery {
    public static final String STATS_QUERY = """
            SELECT
                (SELECT COUNT(*) FROM usuario) AS total_usuarios,
                (SELECT COUNT(*) FROM producto) AS total_productos,
                (SELECT COUNT(*) FROM pedido) AS total_pedidos,
                (SELECT SUM(total) FROM pedido) AS total_billed
            """;
}
