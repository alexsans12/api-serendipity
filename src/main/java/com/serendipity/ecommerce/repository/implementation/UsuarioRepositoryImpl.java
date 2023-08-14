package com.serendipity.ecommerce.repository.implementation;

import com.serendipity.ecommerce.domain.Rol;
import com.serendipity.ecommerce.domain.Usuario;
import com.serendipity.ecommerce.exception.ApiException;
import com.serendipity.ecommerce.repository.RolRepository;
import com.serendipity.ecommerce.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import static com.serendipity.ecommerce.enumeration.RolType.ROLE_USER;
import static com.serendipity.ecommerce.enumeration.VerificationType.*;
import static com.serendipity.ecommerce.query.UsuarioQuery.*;
import static java.util.Objects.requireNonNull;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UsuarioRepositoryImpl implements UsuarioRepository<Usuario> {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RolRepository<Rol> rolRepository;
    private final BCryptPasswordEncoder encoder;

    @Override
    public Usuario create(Usuario usuario) {
        // Check the email is not already in use
        if(getEmailCount(usuario.getEmail().trim().toLowerCase()) > 0) throw new ApiException("El correo electrónico ya está en uso. Por favor, ingrese otro correo electrónico y vuelva a intentarlo.");
        // Save new user
        try {
            KeyHolder holder = new GeneratedKeyHolder();
            SqlParameterSource parameters = getSqlParameterSource(usuario);
            jdbcTemplate.update(INSERT_USUARIO_QUERY, parameters, holder);
            usuario.setIdUsuario(requireNonNull(holder.getKey()).longValue());
            // Add role to the user
            rolRepository.addRolToUsuario(usuario.getIdUsuario(), ROLE_USER.name());
            // Send verification email
            String verificationUrl = getVerificationUrl(UUID.randomUUID().toString(), ACCOUNT.getType());
            // Save URL in verification table
            jdbcTemplate.update(INSERT_VERIFICACION_CUENTA_URL_QUERY, Map.of("idUsuario", usuario.getIdUsuario(), "url", verificationUrl));
            // Send email to user with verification URL
            //emailService.sendVerificationUrl(usuario.getNombre(), usuario.getEmail(), verificationUrl, ACCOUNT.getType());
            usuario.setEstado(false);
            // Return the newly created user
            return usuario;
            // If any error, throw an exception with proper message
        } catch (Exception exception) {
            throw new ApiException("Un error inesperado ha ocurrido. Por favor, inténtelo de nuevo más tarde.");
        }
    }

    @Override
    public Collection<Usuario> findAll(int page, int pageSize) {
        return null;
    }

    @Override
    public Usuario getById(Long id) {
        return null;
    }

    @Override
    public Usuario update(Usuario data) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    private Integer getEmailCount(String email) {
        return jdbcTemplate.queryForObject(COUNT_USUARIO_EMAIL_QUERY, Map.of("email", email), Integer.class);
    }

    private SqlParameterSource getSqlParameterSource(Usuario usuario) {
        return new MapSqlParameterSource()
                .addValue("nombre", usuario.getNombre())
                .addValue("email", usuario.getEmail().trim().toLowerCase())
                .addValue("password", encoder.encode(usuario.getPassword()));
    }

    private String getVerificationUrl(String token, String type) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/usuarios/verify/" + type + "/" + token)
                .toUriString();
    }
}
