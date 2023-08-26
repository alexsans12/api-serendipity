package com.serendipity.ecommerce.resource;

import com.serendipity.ecommerce.domain.HttpResponse;
import com.serendipity.ecommerce.domain.Usuario;
import com.serendipity.ecommerce.domain.UsuarioPrincipal;
import com.serendipity.ecommerce.dto.UsuarioDTO;
import com.serendipity.ecommerce.exception.ApiException;
import com.serendipity.ecommerce.form.LoginForm;
import com.serendipity.ecommerce.form.UpdateProfileForm;
import com.serendipity.ecommerce.provider.TokenProvider;
import com.serendipity.ecommerce.service.RolService;
import com.serendipity.ecommerce.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static com.serendipity.ecommerce.dtomapper.UsuarioDTOMapper.toUsuario;
import static com.serendipity.ecommerce.utils.ExceptionUtils.processError;
import static com.serendipity.ecommerce.utils.UsuarioUtils.getAuthenticatedUsuario;
import static com.serendipity.ecommerce.utils.UsuarioUtils.getLoggedInUsuario;
import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.security.authentication.UsernamePasswordAuthenticationToken.unauthenticated;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@RestController
@RequestMapping("/api/v1/usuario")
@RequiredArgsConstructor
public class UsuarioResource {
    private static final String TOKEN_PREFIX = "Bearer ";

    private final UsuarioService usuarioService;
    private final RolService rolService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    @PostMapping("/login")
    public ResponseEntity<HttpResponse> login(@RequestBody @Valid LoginForm loginForm) {
        Authentication authentication = authenticate(loginForm.getEmail(), loginForm.getPassword());
        UsuarioDTO usuario = getLoggedInUsuario(authentication);
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
                        .data(of("usuario", usuario))
                        .message("Perfil de usuario obtenido exitosamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build());
    }

    @PatchMapping("/update/profile")
    public ResponseEntity<HttpResponse> updateUser(@RequestBody @Valid UpdateProfileForm usuario, Authentication authentication) {
        usuario.setModificadoPor(getAuthenticatedUsuario(authentication).getIdUsuario());
        UsuarioDTO updateUsuario = usuarioService.updateUsuarioDetails(usuario);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("usuario", updateUsuario))
                        .message("Perfil de usuario actualizado exitosamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build());
    }

    @GetMapping("/verify/code/{email}/{code}")
    public ResponseEntity<HttpResponse> verifyCode(@PathVariable("email") String email, @PathVariable("code") String code) {
        UsuarioDTO usuario = usuarioService.verifyCode(email, code);
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

    // START - Restablecer contraseña cuando el usuario no está logueado
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
                        .message("Porfavor entra una nueva contraseña")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build());
    }

    @PostMapping("/reset-password/{key}/{password}/{confirmPassword}")
    public ResponseEntity<HttpResponse> resetPasswordWithKey(@PathVariable("key") String key, @PathVariable("password") String password, @PathVariable("confirmPassword") String confirmPassword) {
        usuarioService.renewPassword(key, password, confirmPassword);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .message("La contraseña ha sido cambiada exitosamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build());
    }

    // END - Restablecer contraseña cuando el usuario no está logueado

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

    private Authentication authenticate(String email, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(unauthenticated(email, password));
            return authentication;
        } catch (Exception exception) {
            //processError(request, response, exception);
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
