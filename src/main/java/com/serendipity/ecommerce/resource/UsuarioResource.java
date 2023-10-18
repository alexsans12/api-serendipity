package com.serendipity.ecommerce.resource;

import com.serendipity.ecommerce.domain.HttpResponse;
import com.serendipity.ecommerce.domain.Usuario;
import com.serendipity.ecommerce.domain.UsuarioPrincipal;
import com.serendipity.ecommerce.dto.UsuarioDTO;
import com.serendipity.ecommerce.event.NewUsuarioEvento;
import com.serendipity.ecommerce.exception.ApiException;
import com.serendipity.ecommerce.form.*;
import com.serendipity.ecommerce.provider.TokenProvider;
import com.serendipity.ecommerce.service.EventoService;
import com.serendipity.ecommerce.service.RolService;
import com.serendipity.ecommerce.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.serendipity.ecommerce.constant.Constants.TOKEN_PREFIX;
import static com.serendipity.ecommerce.dtomapper.UsuarioDTOMapper.toUsuario;
import static com.serendipity.ecommerce.enumeration.EventoType.*;
import static com.serendipity.ecommerce.utils.ExceptionUtils.processError;
import static com.serendipity.ecommerce.utils.UsuarioUtils.getAuthenticatedUsuario;
import static com.serendipity.ecommerce.utils.UsuarioUtils.getLoggedInUsuario;
import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;
import static org.springframework.security.authentication.UsernamePasswordAuthenticationToken.unauthenticated;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@RestController
@RequestMapping("/api/v1/usuario")
@RequiredArgsConstructor
public class UsuarioResource {
    private final UsuarioService usuarioService;
    private final RolService rolService;
    private final EventoService eventoService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final ApplicationEventPublisher eventPublisher;

    @PostMapping("/login")
    public ResponseEntity<HttpResponse> login(@RequestBody @Valid LoginForm loginForm) {
        UsuarioDTO usuario = authenticate(loginForm.getEmail(), loginForm.getPassword());
        return usuario.isUtilizaMfa() ? sendVerificationCode(usuario) : sendResponse(usuario);
    }

    @PostMapping("/register")
    public ResponseEntity<HttpResponse> saveUsuario(@RequestBody @Valid Usuario usuario) {
        UsuarioDTO usuarioDTO = usuarioService.createUsuario(usuario);
        return ResponseEntity.created(getUri()).body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("usuario", usuarioDTO))
                        .message("Usuario creado exitosamente")
                        .httpStatus(CREATED)
                        .httpStatusCode(CREATED.value())
                        .build());
    }

    @GetMapping("/profile")
    public ResponseEntity<HttpResponse> profile(Authentication authentication) {
        UsuarioDTO usuario = usuarioService.getUsuarioByEmail(getAuthenticatedUsuario(authentication).getEmail());
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("usuario", usuario, "eventos", eventoService.getEventsByUsuarioId(usuario.getIdUsuario()),"roles", rolService.getRoles()))
                        .message("Perfil de usuario obtenido exitosamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build());
    }

    @PatchMapping("/update/profile")
    public ResponseEntity<HttpResponse> updateUser(@RequestBody @Valid UpdateProfileForm usuario, Authentication authentication) {
        usuario.setModificadoPor(getAuthenticatedUsuario(authentication).getIdUsuario());
        UsuarioDTO updateUsuario = usuarioService.updateUsuarioDetails(usuario);
        eventPublisher.publishEvent(new NewUsuarioEvento(ACTUALIZACION_PERFIL, usuario.getEmail()));
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("usuario", updateUsuario, "eventos", eventoService.getEventsByUsuarioId(usuario.getIdUsuario()),"roles", rolService.getRoles()))
                        .message("Perfil de usuario actualizado exitosamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build());
    }

    @GetMapping("/verify/code/{email}/{code}")
    public ResponseEntity<HttpResponse> verifyCode(@PathVariable("email") String email, @PathVariable("code") String code) {
        UsuarioDTO usuario = usuarioService.verifyCode(email, code);
        eventPublisher.publishEvent(new NewUsuarioEvento(EXITO_LOGIN, usuario.getEmail()));
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("usuario", usuario, "access_token", tokenProvider.createAccessToken(getUsuarioPrincipal(usuario))
                                , "refresh_token", tokenProvider.createRefreshToken(getUsuarioPrincipal(usuario))))
                        .message("Inicio de sesión exitoso")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build());
    }

    // INICIO - Reset Password cuando el usuario no esta logueado
    @GetMapping("/reset-password/{email}")
    public ResponseEntity<HttpResponse> resetPassword(@PathVariable("email") String email) {
        usuarioService.resetPassword(email);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .message("Correo enviado. Por favor revise su bandeja de entrada")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build());
    }

    @GetMapping("/verify/password/{key}")
    public ResponseEntity<HttpResponse> verifyPasswordUrl(@PathVariable("key") String key) {
        UsuarioDTO usuario = usuarioService.verifyPasswordKey(key);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("usuario", usuario))
                        .message("Porfavor ingresa una nueva contraseña")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build());
    }

    @PutMapping("/new/password")
    public ResponseEntity<HttpResponse> resetPasswordWithKey(@RequestBody @Valid NewPasswordForm form) {
        usuarioService.updatePassword(form.getIdUsuario(), form.getPassword(), form.getConfirmPassword());
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .message("La contraseña ha sido cambiada exitosamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build());
    }

    // FIN - Reset Password cuando el usuario no esta logueado

    @GetMapping("/verify/account/{key}")
    public ResponseEntity<HttpResponse> verifyAccount(@PathVariable("key") String key) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .message(usuarioService.verifyAccountKey(key).isEstado() ? "La cuenta ya ha sido verificada." : "La cuenta ha sido verificada.")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build());
    }

    @PatchMapping("/update/password")
    public ResponseEntity<HttpResponse> updatePassword(Authentication authentication, @RequestBody @Valid UpdatePasswordForm form) {
        UsuarioDTO usuario = getAuthenticatedUsuario(authentication);
        usuarioService.updatePassword(usuario.getIdUsuario(), form.getCurrentPassword(), form.getNewPassword(), form.getConfirmNewPassword());
        eventPublisher.publishEvent(new NewUsuarioEvento(ACTUALIZACION_CONTRASENA, usuario.getEmail()));
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .data(of("usuario", usuarioService.getUsuarioById(usuario.getIdUsuario()), "eventos", eventoService.getEventsByUsuarioId(usuario.getIdUsuario()), "roles", rolService.getRoles()))
                        .timestamp(now().toString())
                        .message("Password actualizado exitosamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build());
    }

    @PatchMapping("/update/rol/{rol}")
    public ResponseEntity<HttpResponse> updateRolUsuario(Authentication authentication, @PathVariable("rol") String rol) {
        UsuarioDTO usuarioDTO = getAuthenticatedUsuario(authentication);
        usuarioService.updateRolUsuario(usuarioDTO.getIdUsuario(), rol);
        eventPublisher.publishEvent(new NewUsuarioEvento(ACTUALIZACION_ROL, usuarioDTO.getEmail()));
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .data(of("usuario", usuarioService.getUsuarioById(usuarioDTO.getIdUsuario()), "eventos", eventoService.getEventsByUsuarioId(usuarioDTO.getIdUsuario()),"roles", rolService.getRoles()))
                        .timestamp(now().toString())
                        .message("Rol de usuario actualizado exitosamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build());
    }

    @PatchMapping("/update/settings")
    public ResponseEntity<HttpResponse> updateAccount(Authentication authentication, @RequestBody @Valid SettingsForm form) {
        UsuarioDTO usuarioDTO = getAuthenticatedUsuario(authentication);
        usuarioService.updateAccountSettings(usuarioDTO.getIdUsuario(), form.getEnabled());
        eventPublisher.publishEvent(new NewUsuarioEvento(ACTUALIZACION_CONFIGURACION_CUENTA, usuarioDTO.getEmail()));
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .data(of("usuario", usuarioService.getUsuarioById(usuarioDTO.getIdUsuario()), "eventos", eventoService.getEventsByUsuarioId(usuarioDTO.getIdUsuario()), "roles", rolService.getRoles()))
                        .timestamp(now().toString())
                        .message("Configuración de cuenta actualizada exitosamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build());
    }

    @PatchMapping("/toggle/mfa")
    public ResponseEntity<HttpResponse> toggleMfa(Authentication authentication) {
        UsuarioDTO usuarioDTO = usuarioService.toggleMfa(getAuthenticatedUsuario(authentication).getEmail());
        eventPublisher.publishEvent(new NewUsuarioEvento(ACTUALIZACION_MFA, usuarioDTO.getEmail()));
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .data(of("usuario", usuarioService.getUsuarioById(usuarioDTO.getIdUsuario()), "eventos", eventoService.getEventsByUsuarioId(usuarioDTO.getIdUsuario()), "roles", rolService.getRoles()))
                        .timestamp(now().toString())
                        .message("Autenticación de multifactores actualizada exitosamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build());
    }

    @PatchMapping("/update/image")
    public ResponseEntity<HttpResponse> updateProfileImage(Authentication authentication, @RequestParam("image") MultipartFile image) {
        UsuarioDTO usuarioDTO = getAuthenticatedUsuario(authentication);
        usuarioService.updateImage(usuarioDTO, image);
        eventPublisher.publishEvent(new NewUsuarioEvento(ACTUALIZACION_FOTO_PERFIL, usuarioDTO.getEmail()));
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .data(of("usuario", usuarioService.getUsuarioById(usuarioDTO.getIdUsuario()), "eventos", eventoService.getEventsByUsuarioId(usuarioDTO.getIdUsuario()), "roles", rolService.getRoles()))
                        .timestamp(now().toString())
                        .message("Imagen de perfil actualizada exitosamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build());
    }

    @GetMapping(value = "/image/{fileName}", produces = IMAGE_PNG_VALUE)
    public byte[] getProfileImage(@PathVariable("fileName") String fileName) {
        try {
            return Files.readAllBytes(Paths.get(System.getProperty("user.home") + "/Downloads/images/" + fileName));
        } catch (IOException e) {
            throw new ApiException("Error al obtener la imagen del usuario " + fileName);
        }
    }

    @GetMapping("/refresh/token")
    public ResponseEntity<HttpResponse> refreshToken(HttpServletRequest request) {
        if (isHeaderTokenValid(request)) {
            String token = request.getHeader(AUTHORIZATION).substring(TOKEN_PREFIX.length());
            UsuarioDTO usuario = usuarioService.getUsuarioById(tokenProvider.getSubject(token, request));
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .data(of("usuario", usuario, "access_token", tokenProvider.createAccessToken(getUsuarioPrincipal(usuario))
                                    , "refresh_token", tokenProvider.createRefreshToken(getUsuarioPrincipal(usuario))))
                            .message("Token de acceso renovado exitosamente")
                            .httpStatus(OK)
                            .httpStatusCode(OK.value())
                            .build());
        } else {
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .message("No se encuentra el refresh token o es invalido")
                            .developerMessage("No se encuentra el refresh token o es invalido")
                            .httpStatus(BAD_REQUEST)
                            .httpStatusCode(BAD_REQUEST.value())
                            .build());
        }
    }

    private Boolean isHeaderTokenValid(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION) != null
                && request.getHeader(AUTHORIZATION).startsWith(TOKEN_PREFIX)
                && tokenProvider.isTokenValid(tokenProvider.getSubject(request.getHeader(AUTHORIZATION).substring(TOKEN_PREFIX.length()), request),
                request.getHeader(AUTHORIZATION).substring(TOKEN_PREFIX.length())
        );
    }

    @RequestMapping("/error")
    public ResponseEntity<HttpResponse> handleError(HttpServletRequest request) {
        return ResponseEntity.badRequest().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .reason("No hay asignacion para esta solicitud " + request.getMethod() + " para esta ruta en el servidor")
                        .httpStatus(BAD_REQUEST)
                        .httpStatusCode(BAD_REQUEST.value())
                        .build());
    }

    /*@RequestMapping("/error")
    public ResponseEntity<HttpResponse> handleError(HttpServletRequest request) {
        return new ResponseEntity<>(HttpResponse.builder()
                        .timestamp(now().toString())
                        .reason("No hay asignacion para esta solicitud " + request.getMethod() + " para esta ruta en el servidor")
                        .httpStatus(NOT_FOUND)
                        .httpStatusCode(NOT_FOUND.value())
                        .build(), NOT_FOUND);
    }*/

    private UsuarioDTO authenticate(String email, String password) {
        try {
            if(null != usuarioService.getUsuarioByEmail(email))
                eventPublisher.publishEvent(new NewUsuarioEvento(INTENTO_LOGIN, email));

            Authentication authentication = authenticationManager.authenticate(unauthenticated(email, password));
            UsuarioDTO loggedInUsuario = getLoggedInUsuario(authentication);

            if (!loggedInUsuario.isUtilizaMfa()) eventPublisher.publishEvent(new NewUsuarioEvento(EXITO_LOGIN, email));

            return loggedInUsuario;
        } catch (Exception exception) {
            if(null != usuarioService.getUsuarioByEmail(email))
                eventPublisher.publishEvent(new NewUsuarioEvento(FALLO_LOGIN, email));
            processError(request, response, exception);
            throw new ApiException(exception.getMessage());
        }
    }

    private URI getUri() {
        return URI.create(fromCurrentContextPath().path("/api/v1/usuario/get/<usuarioId>").toUriString());
    }

    private ResponseEntity<HttpResponse> sendResponse(UsuarioDTO usuario) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("usuario", usuario, "access_token", tokenProvider.createAccessToken(getUsuarioPrincipal(usuario))
                        , "refresh_token", tokenProvider.createRefreshToken(getUsuarioPrincipal(usuario))))
                        .message("Inicio de sesión exitoso")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build());
    }

    private UsuarioPrincipal getUsuarioPrincipal(UsuarioDTO usuario) {
        return new UsuarioPrincipal(toUsuario(usuarioService.getUsuarioByEmail(usuario.getEmail())), rolService.getRolByUsuarioId(usuario.getIdUsuario()));
    }

    private ResponseEntity<HttpResponse> sendVerificationCode(UsuarioDTO usuario) {
        usuarioService.sendVerificationCode(usuario);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("usuario", usuario))
                        .message("Codigo de verificación enviado.")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build());
    }
}
