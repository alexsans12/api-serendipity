package com.serendipity.ecommerce.repository;

import com.serendipity.ecommerce.domain.UsuarioEvento;
import com.serendipity.ecommerce.enumeration.EventoType;

import java.util.Collection;

public interface EventoRepository {
    Collection<UsuarioEvento> getEventsByUsuarioId(Long usuarioId);
    void addUsuarioEvent(String email, EventoType eventoType, String dispositivo, String ipAddress);
    void addUsuarioEvent(Long usuarioId, EventoType eventoType, String dispositivo, String ipAddress);
}
