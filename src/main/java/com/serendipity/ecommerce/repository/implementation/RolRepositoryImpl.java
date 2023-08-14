package com.serendipity.ecommerce.repository.implementation;

import com.serendipity.ecommerce.domain.Rol;
import com.serendipity.ecommerce.exception.ApiException;
import com.serendipity.ecommerce.repository.RolRepository;
import com.serendipity.ecommerce.rowmapper.RolRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;

import static com.serendipity.ecommerce.enumeration.RolType.*;
import static com.serendipity.ecommerce.query.RolQuery.*;
import static java.util.Objects.requireNonNull;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RolRepositoryImpl implements RolRepository<Rol> {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    @Override
    public Rol create(Rol data) {
        return null;
    }

    @Override
    public Collection<Rol> findAll(int page, int pageSize) {
        return null;
    }

    @Override
    public Rol getById(Long id) {
        return null;
    }

    @Override
    public Rol update(Rol data) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    @Override
    public void addRolToUsuario(Long usuarioId, String rolNombre) {
        log.info("Agregando rol {} a usuario con ID: {}", rolNombre, usuarioId);
        try {
            Rol rol = jdbcTemplate.queryForObject(SELECT_ROL_BY_NAME_QUERY, Map.of("rolNombre", rolNombre), new RolRowMapper());
            jdbcTemplate.update(INSERT_ROL_TO_USUARIO_QUERY, Map.of("usuarioId", usuarioId, "rolId", requireNonNull(rol).getIdRol()));

        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("No se encontró ningún rol por nombre: " + ROLE_USER.name());
        } catch (Exception exception) {
            throw new ApiException("Un error inesperado ha ocurrido. Por favor, inténtelo de nuevo más tarde.");
        }
    }

    @Override
    public Rol getRolByUsuarioId(Long usuarioId) {
        return null;
    }

    @Override
    public Rol getRolByUsuarioEmail(String email) {
        return null;
    }

    @Override
    public void updateRolUsuario(Long usuarioId, String rolNombre) {

    }
}
