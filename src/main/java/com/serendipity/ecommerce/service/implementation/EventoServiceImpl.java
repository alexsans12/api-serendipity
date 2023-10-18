package com.serendipity.ecommerce.service.implementation;

import com.serendipity.ecommerce.domain.UsuarioEvento;
import com.serendipity.ecommerce.enumeration.EventoType;
import com.serendipity.ecommerce.repository.EventoRepository;
import com.serendipity.ecommerce.service.EventoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class EventoServiceImpl implements EventoService {
    private final EventoRepository eventoRepository;

    @Override
    public Collection<UsuarioEvento> getEventsByUsuarioId(Long usuarioId) {
        return eventoRepository.getEventsByUsuarioId(usuarioId);
    }

    @Override
    public void addUsuarioEvent(String email, EventoType eventoType, String dispositivo, String ipAddress) {
        eventoRepository.addUsuarioEvent(email, eventoType, dispositivo, ipAddress);
    }

    @Override
    public void addUsuarioEvent(Long usuarioId, EventoType eventoType, String dispositivo, String ipAddress) {
        eventoRepository.addUsuarioEvent(usuarioId, eventoType, dispositivo, ipAddress);
    }
}
