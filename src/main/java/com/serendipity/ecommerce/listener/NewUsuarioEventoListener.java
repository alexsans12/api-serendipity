package com.serendipity.ecommerce.listener;

import com.serendipity.ecommerce.event.NewUsuarioEvento;
import com.serendipity.ecommerce.service.EventoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.serendipity.ecommerce.utils.RequestUtils.getDevice;
import static com.serendipity.ecommerce.utils.RequestUtils.getIpAddress;

@Component
@RequiredArgsConstructor
public class NewUsuarioEventoListener {
    private final EventoService eventoService;
    private final HttpServletRequest servletRequest;

    @EventListener
    public void onNewUsuarioEvento(NewUsuarioEvento usuarioEvento) {
        eventoService.addUsuarioEvent(usuarioEvento.getEmail(), usuarioEvento.getEventoType(), getDevice(servletRequest), getIpAddress(servletRequest));
    }
}
