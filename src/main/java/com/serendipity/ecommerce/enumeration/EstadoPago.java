package com.serendipity.ecommerce.enumeration;

public enum EstadoPago {
    PENDIENTE ("Pendiente"),
    APROBADO ("Aprobado"),
    RECHAZADO ("Rechazado");

    private final String estado;

    EstadoPago(String estado) {
        this.estado = estado;
    }

    public String getDisplayName() {
        return estado;
    }

    public static EstadoPago fromDisplayName(String estado) {
        for (EstadoPago estadoPago : EstadoPago.values()) {
            if (estadoPago.estado.equalsIgnoreCase(estado)) {
                return estadoPago;
            }
        }
        throw new IllegalArgumentException("Unknown display name: " + estado);
    }
}
