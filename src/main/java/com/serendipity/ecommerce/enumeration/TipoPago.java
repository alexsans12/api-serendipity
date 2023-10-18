package com.serendipity.ecommerce.enumeration;

public enum TipoPago {
    TARJETA_DE_CREDITO("Tarjeta de crédito"),
    TARJETA_DE_DEBITO("Tarjeta de débito"),
    PAYPAL("Paypal");
    private final String estado;

    TipoPago(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

    public static TipoPago fromDisplayName(String estado) {
        for (TipoPago tipoPago : TipoPago.values()) {
            if (tipoPago.estado.equalsIgnoreCase(estado)) {
                return tipoPago;
            }
        }
        throw new IllegalArgumentException("Unknown estado name: " + estado);
    }
}
