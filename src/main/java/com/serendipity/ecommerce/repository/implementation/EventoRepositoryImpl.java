package com.serendipity.ecommerce.repository.implementation;

import com.serendipity.ecommerce.domain.UsuarioEvento;
import com.serendipity.ecommerce.enumeration.EventoType;
import com.serendipity.ecommerce.repository.EventoRepository;
import com.serendipity.ecommerce.rowmapper.UsuarioEventoRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;

import static com.serendipity.ecommerce.query.EventoQuery.*;
import static java.util.Map.of;

@Repository
@RequiredArgsConstructor
@Slf4j
public class EventoRepositoryImpl implements EventoRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    @Override
    public Collection<UsuarioEvento> getEventsByUsuarioId(Long usuarioId) {
        return jdbcTemplate.query(SELECT_EVENTOS_BY_USUARIO_ID_QUERY, of("usuarioId", usuarioId), new UsuarioEventoRowMapper());
    }

    @Override
    public void addUsuarioEvent(String email, EventoType eventoType, String dispositivo, String ipAddress) {
        jdbcTemplate.update(INSERT_EVENTO_BY_USUARIO_EMAIL_QUERY, of("email", email, "eventoType", eventoType.toString(), "dispositivo", dispositivo, "ipAddress", ipAddress));
    }

    @Override
    public void addUsuarioEvent(Long usuarioId, EventoType eventoType, String dispositivo, String ipAddress) {
        jdbcTemplate.update(INSERT_EVENTO_BY_USUARIO_ID_QUERY, of("id_usuario", usuarioId, "eventoType", eventoType.toString(), "dispositivo", dispositivo, "ipAddress", ipAddress));
    }
}
