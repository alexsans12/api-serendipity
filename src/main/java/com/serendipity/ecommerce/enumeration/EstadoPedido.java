package com.serendipity.ecommerce.enumeration;

public enum EstadoPedido {
    EN_PROCESO("En proceso"),
    EN_CAMINO("En camino"),
    ENTREGADO("Entregado"),
    EMPACADO("Empacado"),
    CANCELADO("Cancelado");

    private String estado;

    EstadoPedido(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }
}
