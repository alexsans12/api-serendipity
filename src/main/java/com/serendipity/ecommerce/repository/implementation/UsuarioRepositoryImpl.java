package com.serendipity.ecommerce.repository.implementation;

import com.serendipity.ecommerce.domain.Rol;
import com.serendipity.ecommerce.domain.Usuario;
import com.serendipity.ecommerce.domain.UsuarioPrincipal;
import com.serendipity.ecommerce.dto.UsuarioDTO;
import com.serendipity.ecommerce.enumeration.VerificationType;
import com.serendipity.ecommerce.exception.ApiException;
import com.serendipity.ecommerce.form.UpdateProfileForm;
import com.serendipity.ecommerce.repository.RolRepository;
import com.serendipity.ecommerce.repository.UsuarioRepository;
import com.serendipity.ecommerce.rowmapper.RolRowMapper;
import com.serendipity.ecommerce.rowmapper.UsuarioRowMapper;
import com.serendipity.ecommerce.service.EmailService;
import com.serendipity.ecommerce.utils.SmsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.serendipity.ecommerce.constant.Constants.DATE_FORMAT;
import static com.serendipity.ecommerce.enumeration.RolType.ROL_USUARIO;
import static com.serendipity.ecommerce.enumeration.VerificationType.ACCOUNT;
import static com.serendipity.ecommerce.enumeration.VerificationType.PASSWORD;
import static com.serendipity.ecommerce.query.RolQuery.SELECT_ROL_BY_NAME_QUERY;
import static com.serendipity.ecommerce.query.RolQuery.UPDATE_ROL_TO_USUARIO_QUERY;
import static com.serendipity.ecommerce.query.UsuarioQuery.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.time.DateFormatUtils.format;
import static org.apache.commons.lang3.time.DateUtils.addDays;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UsuarioRepositoryImpl implements UsuarioRepository<Usuario>, UserDetailsService {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RolRepository<Rol> rolRepository;
    private final BCryptPasswordEncoder encoder;
    private final EmailService emailService;

    @Autowired
    private SmsUtils smsUtils;

    @Value("${ui.app.url}")
    private String uiAppUrl;

    @Override
    public Usuario create(Usuario usuario) {
        // Check the email is not already in use
        if (getEmailCount(usuario.getEmail().trim().toLowerCase()) > 0)
            throw new ApiException("El correo electrónico ya está en uso. Por favor, ingrese otro correo electrónico y vuelva a intentarlo.");
        // Save new user
        try {
            KeyHolder holder = new GeneratedKeyHolder();
            SqlParameterSource parameters = getSqlParameterSource(usuario);
            jdbcTemplate.update(INSERT_USUARIO_QUERY, parameters, holder);
            usuario.setIdUsuario(requireNonNull(holder.getKey()).longValue());
            // Add role to the user
            rolRepository.addRolToUsuario(usuario.getIdUsuario(), ROL_USUARIO.name());
            // Send verification email
            String verificationUrl = getVerificationUrl(UUID.randomUUID().toString(), ACCOUNT.getType());
            // Save URL in verification table
            jdbcTemplate.update(INSERT_VERIFICACION_CUENTA_URL_QUERY, of("idUsuario", usuario.getIdUsuario(), "url", verificationUrl, "fecha", now().plusDays(1)));
            // Send email to user with verification URL
            sendEmail(usuario.getNombre(), usuario.getEmail(), verificationUrl, ACCOUNT);

            usuario.setEstado(false);
            // Return the newly created user
            return usuario;
            // If any error, throw an exception with proper message
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("Un error inesperado ha ocurrido. Por favor, inténtelo de nuevo más tarde.");
        }
    }

    @Override
    public Usuario getById(Long id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_USUARIO_BY_ID_QUERY, of("id", id), new UsuarioRowMapper());
        } catch (EmptyResultDataAccessException exception) {
            // If any error, throw an exception with proper message
            throw new ApiException("Usuario no encontrado por id: " + id);
        } catch (Exception exception) {
            // If any error, throw an exception with proper message
            log.error(exception.getMessage());
            throw new ApiException("Un error inesperado ha ocurrido. Por favor, inténtelo de nuevo más tarde.");
        }
    }

    @Override
    public Usuario update(Usuario data) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = getUsuarioByEmail(email);
        if (usuario == null) {
            log.error("Usuario no encontrado en la base de datos");
            throw new UsernameNotFoundException("Usuario no encontrado en la base de datos");
        } else {
            log.info("Usuario encontrado en la base de datos: {}", email);
            return new UsuarioPrincipal(usuario, rolRepository.getRolByUsuarioId(usuario.getIdUsuario()));
        }
    }

    @Override
    public Usuario getUsuarioByEmail(String email) {
        try {
            return jdbcTemplate.queryForObject(SELECT_USUARIO_BY_EMAIL_QUERY, of("email", email), new UsuarioRowMapper());
        } catch (EmptyResultDataAccessException exception) {
            // If any error, throw an exception with proper message
            throw new ApiException("Usuario no encontrado por email: " + email);
        } catch (Exception exception) {
            // If any error, throw an exception with proper message
            log.error(exception.getMessage());
            throw new ApiException("Un error inesperado ha ocurrido. Por favor, inténtelo de nuevo más tarde.");
        }
    }

    @Override
    public void sendVerificationCode(UsuarioDTO usuario) {
        String expirationDate = format(addDays(new Date(), 1), DATE_FORMAT);
        String verificationCode = randomAlphabetic(8).toUpperCase();
        try {
            jdbcTemplate.update(DELETE_VERIFICATION_BY_USUARIO_ID, of("id", usuario.getIdUsuario()));
            jdbcTemplate.update(INSERT_VERIFICATION_CODE_QUERY, of("usuario_id", usuario.getIdUsuario(), "codigo", verificationCode, "fecha_expiracion", expirationDate));
            smsUtils.sendSMS(usuario.getTelefono(), "From: Serendipity Ecommerce\n\nYour verification code is: " + verificationCode);
            log.info("Código de verificación enviado: {}", verificationCode);
        } catch (Exception exception) {
            // If any error, throw an exception with proper message
            log.error(exception.getMessage());
            throw new ApiException("Un error inesperado ha ocurrido. Por favor, inténtelo de nuevo más tarde.");
        }
    }

    @Override
    public Usuario verifyCode(String email, String code) {
        if (isVerificationCodeExpired(code))
            throw new ApiException("Este código de verificación ha expirado. Por favor, inicie sesión nuevamente.");

        try {
            Usuario usuarioByCode = jdbcTemplate.queryForObject(SELECT_USUARIO_BY_USUARIO_CODE_QUERY, of("code", code), new UsuarioRowMapper());
            Usuario usuarioByEmail = jdbcTemplate.queryForObject(SELECT_USUARIO_BY_EMAIL_QUERY, of("email", email), new UsuarioRowMapper());
            if (requireNonNull(usuarioByCode).getEmail().equalsIgnoreCase(requireNonNull(usuarioByEmail).getEmail())) {
                jdbcTemplate.update(DELETE_CODE, of("code", code));
                return usuarioByCode;
            } else {
                throw new ApiException("Código de verificación inválido. Por favor intentelo de nuevo.");
            }
        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("No se pudo encontrar el registro.");
        } catch (Exception exception) {
            throw new ApiException("Un error inesperado ha ocurrido. Por favor, inténtelo de nuevo más tarde.");
        }
    }

    @Override
    public void resetPassword(String email) {
        if (getEmailCount(email.trim().toLowerCase()) <= 0)
            throw new ApiException("No hay cuenta asociada a este correo electrónico.");

        try {
            String expirationDate = format(addDays(new Date(), 1), DATE_FORMAT);
            Usuario usuario = getUsuarioByEmail(email);
            String verificationUrl = getVerificationUrl(UUID.randomUUID().toString(), PASSWORD.getType());
            jdbcTemplate.update(DELETE_PASSWORD_VERIFICATION_BY_USUARIO_ID_QUERY, of("id", usuario.getIdUsuario()));
            jdbcTemplate.update(INSERT_PASSWORD_VERIFICATION_QUERY, of("id_usuario", usuario.getIdUsuario(), "url", verificationUrl, "fecha_expiracion", expirationDate));
            // TODO send email to user with verification URL
            sendEmail(usuario.getNombre(), usuario.getEmail(), verificationUrl, PASSWORD);
            log.info("URL de restablecimiento de contraseña enviada: {}", verificationUrl);

        } catch (Exception exception) {
            throw new ApiException("Un error inesperado ha ocurrido. Por favor, inténtelo de nuevo más tarde.");
        }
    }

    @Override
    public Usuario verifyPasswordKey(String key) {
        if (isLinkExpired(key, PASSWORD))
            throw new ApiException("Este enlace ha expirado. Por favor, restablesca su contraseña nuevamente.");

        try {
            return jdbcTemplate.queryForObject(SELECT_USUARIO_BY_PASSWORD_URL_QUERY, of("url", getVerificationUrl(key, PASSWORD.getType())), new UsuarioRowMapper());
        } catch (EmptyResultDataAccessException exception) {
            log.error(exception.getMessage());
            throw new ApiException("Este link no es válido. Por favor, restablesca su contraseña nuevamente");
        } catch (Exception exception) {
            throw new ApiException("Un error inesperado ha ocurrido. Por favor, inténtelo de nuevo más tarde.");
        }
    }

    /*@Override
    public void updatePassword(Long idUsuario, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) throw new ApiException("Las contraseñas no coinciden. Por favor, inténtelo de nuevo.");

        try {
            jdbcTemplate.update(UPDATE_USUARIO_PASSWORD_BY_URL_QUERY, of("password", encoder.encode(password),"url", getVerificationUrl(key, PASSWORD.getType())));
            jdbcTemplate.update(DELETE_VERIFICATION_BY_URL_QUERY, of("url", getVerificationUrl(key, PASSWORD.getType())));
        } catch (Exception exception) {
            throw new ApiException("Un error inesperado ha ocurrido. Por favor, inténtelo de nuevo más tarde.");
        }
    }*/

    @Override
    public void updatePassword(Long idUsuario, String password, String confirmPassword) {
        if (!password.equals(confirmPassword))
            throw new ApiException("Las contraseñas no coinciden. Por favor, inténtelo de nuevo.");

        try {
            Usuario usuario = getById(idUsuario);
            jdbcTemplate.update(UPDATE_USUARIO_PASSWORD_BY_USUARIO_ID_QUERY, of("password", encoder.encode(password), "id_usuario", idUsuario));
            jdbcTemplate.update(DELETE_PASSWORD_VERIFICATION_BY_USUARIO_ID_QUERY, of("id", idUsuario));
            if (!usuario.isEstado()) {
                String verificationUrl = getVerificationUrl(UUID.randomUUID().toString(), ACCOUNT.getType());
                // Save URL in verification table
                jdbcTemplate.update(INSERT_VERIFICACION_CUENTA_URL_QUERY, of("idUsuario", usuario.getIdUsuario(), "url", verificationUrl, "fecha", now().plusDays(1)));
                // Send email to user with verification URL
                sendEmail(usuario.getNombre(), usuario.getEmail(), verificationUrl, ACCOUNT);
            }
        } catch (Exception exception) {
            throw new ApiException("Un error inesperado ha ocurrido. Por favor, inténtelo de nuevo más tarde.");
        }
    }

    @Override
    public Usuario verifyAccountKey(String key) {
        if (isLinkAccountExpired(key, ACCOUNT)) {
            Usuario usuario = jdbcTemplate.queryForObject(SELECT_USUARIO_BY_ACCOUNT_URL_QUERY, of("url", getVerificationUrl(key, ACCOUNT.getType())), new UsuarioRowMapper());
            // Delete url from verification table
            jdbcTemplate.update(DELETE_ACCOUNT_VERIFICATION_BY_URL_QUERY, of("url", getVerificationUrl(key, ACCOUNT.getType())));
            // Send verification email
            String verificationUrl = getVerificationUrl(UUID.randomUUID().toString(), ACCOUNT.getType());
            // Save URL in verification table
            jdbcTemplate.update(INSERT_VERIFICACION_CUENTA_URL_QUERY, of("idUsuario", usuario.getIdUsuario(), "url", verificationUrl, "fecha", now().plusDays(1)));
            // Send email to user with verification URL
            sendEmail(usuario.getNombre(), usuario.getEmail(), verificationUrl, ACCOUNT);
            throw new ApiException("Este enlace ha expirado. Por favor, se ha enviado un nuevo enlace de verificación a su correo electrónico.");
        }

        try {
            Usuario usuario = jdbcTemplate.queryForObject(SELECT_USUARIO_BY_ACCOUNT_URL_QUERY, of("url", getVerificationUrl(key, ACCOUNT.getType())), new UsuarioRowMapper());
            jdbcTemplate.update(UPDATE_USUARIO_ENABLED_QUERY, of("enabled", true, "id_usuario", requireNonNull(usuario).getIdUsuario()));
            // Delete after update - depends on your requirements
            jdbcTemplate.update(DELETE_ACCOUNT_VERIFICATION_BY_URL_QUERY, of("url", getVerificationUrl(key, ACCOUNT.getType())));
            return usuario;
        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("Este link no es válido.");
        } catch (Exception exception) {
            throw new ApiException("Un error inesperado ha ocurrido. Por favor, inténtelo de nuevo más tarde.");
        }
    }

    @Override
    public Usuario updateUsuarioDetails(UpdateProfileForm usuario) {
        try {
            jdbcTemplate.update(UPDATE_USUARIO_DETAILS_QUERY, getUsuarioDetailsSqlParameterSource(usuario));
            return getById(usuario.getIdUsuario());
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("Un error inesperado ha ocurrido. Por favor, inténtelo de nuevo más tarde.");
        }
    }

    @Override
    public void updatePassword(Long idUsuario, String currentPassword, String newPassword, String confirmNewPassword) {
        if (!newPassword.equals(confirmNewPassword))
            throw new ApiException("Las contraseñas no coinciden. Por favor, inténtelo de nuevo.");

        Usuario usuario = getById(idUsuario);
        if (encoder.matches(currentPassword, usuario.getPassword())) {
            try {
                jdbcTemplate.update(UPDATE_USUARIO_PASSWORD_BY_ID_QUERY, of("password", encoder.encode(newPassword), "id_usuario", idUsuario));
            } catch (Exception exception) {
                throw new ApiException("Un error inesperado ha ocurrido. Por favor, inténtelo de nuevo más tarde.");
            }
        } else {
            throw new ApiException("La contraseña actual no es correcta. Por favor, inténtelo de nuevo.");
        }
    }

    @Override
    public void updateRolUsuario(Long idUsuario, String rol) {
        try {
            Rol rolSelected = jdbcTemplate.queryForObject(SELECT_ROL_BY_NAME_QUERY, of("nombre", rol), new RolRowMapper());
            jdbcTemplate.update(UPDATE_ROL_TO_USUARIO_QUERY, of("usuarioId", idUsuario, "rolId", requireNonNull(rolSelected).getIdRol()));

        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("No se encontró ningún rol por nombre: " + rol);
        } catch (Exception exception) {
            throw new ApiException("Un error inesperado ha ocurrido. Por favor, inténtelo de nuevo más tarde.");
        }
    }

    @Override
    public void updateAccountSettings(Long idUsuario, Boolean enabled) {
        try {
            jdbcTemplate.update(UPDATE_USUARIO_ENABLED_QUERY, of("enabled", enabled, "id_usuario", idUsuario));
        } catch (Exception exception) {
            throw new ApiException("Un error inesperado ha ocurrido. Por favor, inténtelo de nuevo más tarde.");
        }
    }

    @Override
    public Usuario toggleMfa(String email) {
        try {
            Usuario usuario = getUsuarioByEmail(email);
            if (isBlank(usuario.getTelefono())) {
                throw new ApiException("No se puede activar la autenticación de dos factores. Por favor, actualice su número de teléfono e inténtelo de nuevo.");
            }
            usuario.setUtilizaMfa(!usuario.isUtilizaMfa());
            jdbcTemplate.update(UPDATE_USUARIO_MFA_QUERY, of("utiliza_mfa", usuario.isUtilizaMfa(), "email", usuario.getEmail()));
            return usuario;
        } catch (Exception exception) {
            throw new ApiException("No se puede activar la autenticación de dos factores. Por favor, actualice su número de teléfono e inténtelo de nuevo más tarde.");
        }
    }

    @Override
    public void updateImage(UsuarioDTO usuarioDTO, MultipartFile image) {
        String urlFoto = setImageUrl(usuarioDTO.getEmail());
        usuarioDTO.setUrlFoto(urlFoto);
        saveImage(usuarioDTO.getEmail(), image);
        jdbcTemplate.update(UPDATE_USUARIO_IMAGE_QUERY, of("url_foto", urlFoto, "id_usuario", usuarioDTO.getIdUsuario()));
    }

    private void saveImage(String email, MultipartFile image) {
        Path fileStorageLocation = Paths.get(System.getProperty("user.home") + "/Downloads/images/").toAbsolutePath().normalize();
        if (!Files.exists(fileStorageLocation)) {
            try {
                Files.createDirectories(fileStorageLocation);
            } catch (Exception exception) {
                log.error(exception.getMessage());
                throw new ApiException("No se pudo crear el directorio donde se almacenarán los archivos subidos.");
            }
            log.info("Directorio creado con éxito, {}", fileStorageLocation);
        }

        try {
            Files.copy(image.getInputStream(), fileStorageLocation.resolve(email + ".png"), REPLACE_EXISTING);
        } catch (IOException exception) {
            log.error(exception.getMessage());
            throw new ApiException(exception.getMessage());
        }
        log.info("Imagen guardada con éxito, {}", fileStorageLocation);
    }

    private String setImageUrl(String email) {
        return fromCurrentContextPath()
                .path("/api/v1/usuario/image/" + email + ".png")
                .toUriString();
    }

    private void sendEmail(String nombre, String email, String verificationUrl, VerificationType verificationType) {
        /*CompletableFuture.runAsync(() -> emailService.sendVerificationEmail(firstName, email, verificationUrl, verificationType));*/

        ExecutorService emailExecutor = Executors.newFixedThreadPool(10); // or another type of ExecutorService
        CompletableFuture.runAsync(() -> {
            emailService.sendVerificationEmail(nombre, email, verificationUrl, verificationType);
        }, emailExecutor).exceptionally(exception -> {
            System.out.println("Error sending email: " + exception);
            return null;
        });

        /*CompletableFuture.runAsync(() -> {
            try {
                emailService.sendVerificationEmail(nombre, email, verificationUrl, verificationType);
            } catch (Exception exception) {
                throw new ApiException("No se pudo enviar el correo electrónico de verificación. Por favor, inténtelo de nuevo más tarde.");
            }
        });*/

        /*CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                emailService.sendVerificationEmail(nombre, email, verificationUrl, verificationType);
            } catch (Exception exception) {
                throw new ApiException("No se pudo enviar el correo electrónico de verificación. Por favor, inténtelo de nuevo más tarde.");
            }
        });*/
    }

    private Boolean isLinkExpired(String key, VerificationType verificationType) {
        try {
            return jdbcTemplate.queryForObject(SELECT_EXPIRATION_BY_URL_QUERY, of("url", getVerificationUrl(key, verificationType.getType())), Boolean.class);
        } catch (EmptyResultDataAccessException exception) {
            log.error(exception.getMessage());
            throw new ApiException("Este link no es válido. Por favor, restablesca su contraseña nuevamente");
        } catch (Exception exception) {
            throw new ApiException("Un error inesperado ha ocurrido. Por favor, inténtelo de nuevo más tarde.");
        }
    }


    private boolean isLinkAccountExpired(String key, VerificationType verificationType) {
        try {
            return jdbcTemplate.queryForObject(SELECT_EXPIRATION_ACCOUNT_BY_URL_QUERY, of("url", getVerificationUrl(key, verificationType.getType())), Boolean.class);
        } catch (EmptyResultDataAccessException exception) {
            log.error(exception.getMessage());
            throw new ApiException("Este link no es válido. Por favor, restablesca su contraseña nuevamente");
        } catch (Exception exception) {
            throw new ApiException("Un error inesperado ha ocurrido. Por favor, inténtelo de nuevo más tarde.");
        }
    }

    private Boolean isVerificationCodeExpired(String code) {
        try {
            return jdbcTemplate.queryForObject(SELECT_CODE_EXPIRATION_QUERY, of("code", code), Boolean.class);
        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("Este código no es válido. Por favor, inicie sesión nuevamente.");
        } catch (Exception exception) {
            throw new ApiException("Un error inesperado ha ocurrido. Por favor, inténtelo de nuevo más tarde.");
        }
    }

    private Integer getEmailCount(String email) {
        return jdbcTemplate.queryForObject(COUNT_USUARIO_EMAIL_QUERY, of("email", email), Integer.class);
    }

    private SqlParameterSource getSqlParameterSource(Usuario usuario) {
        return new MapSqlParameterSource()
                .addValue("nombre", usuario.getNombre())
                .addValue("apellido", usuario.getApellido())
                .addValue("email", usuario.getEmail().trim().toLowerCase())
                .addValue("password", encoder.encode(usuario.getPassword()))
                .addValue("utiliza_mfa", usuario.isUtilizaMfa())
                .addValue("estado", usuario.isEstado());
    }

    private SqlParameterSource getUsuarioDetailsSqlParameterSource(UpdateProfileForm usuario) {
        return new MapSqlParameterSource()
                .addValue("id_usuario", usuario.getIdUsuario())
                .addValue("nombre", usuario.getNombre())
                .addValue("apellido", usuario.getApellido())
                .addValue("email", usuario.getEmail().trim().toLowerCase())
                .addValue("telefono", usuario.getTelefono())
                .addValue("modificado_por", usuario.getModificadoPor());
    }

    /*private String getVerificationUrl(String token, String type) {
        return fromCurrentContextPath()
                .path("/verify/" + type + "/" + token)
                .toUriString();
    }*/

    private String getVerificationUrl(String token, String type) {
        return UriComponentsBuilder.fromHttpUrl(uiAppUrl)
                .path("/verify/" + type + "/" + token)
                .toUriString();
    }
}
