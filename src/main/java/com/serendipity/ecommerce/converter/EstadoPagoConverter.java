package com.serendipity.ecommerce.converter;

import com.serendipity.ecommerce.enumeration.EstadoPago;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EstadoPagoConverter implements AttributeConverter<EstadoPago, String> {

    @Override
    public String convertToDatabaseColumn(EstadoPago estadoPago) {
        return (estadoPago != null) ? estadoPago.getDisplayName() : null;
    }

    @Override
    public EstadoPago convertToEntityAttribute(String displayName) {
        return EstadoPago.fromDisplayName(displayName);
    }
}
