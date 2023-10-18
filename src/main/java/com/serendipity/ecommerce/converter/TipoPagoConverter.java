package com.serendipity.ecommerce.converter;

import com.serendipity.ecommerce.enumeration.TipoPago;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoPagoConverter implements AttributeConverter<TipoPago, String> {

    @Override
    public String convertToDatabaseColumn(TipoPago tipoPago) {
        return (tipoPago != null) ? tipoPago.getEstado() : null;
    }

    @Override
    public TipoPago convertToEntityAttribute(String displayName) {
        return TipoPago.fromDisplayName(displayName);
    }
}

