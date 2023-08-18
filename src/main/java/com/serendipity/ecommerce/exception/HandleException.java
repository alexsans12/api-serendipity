package com.serendipity.ecommerce.exception;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.serendipity.ecommerce.domain.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class HandleException extends ResponseEntityExceptionHandler implements ErrorController {
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .reason(ex.getMessage())
                        .developerMessage(ex.getMessage())
                        .httpStatus(resolve(statusCode.value()))
                        .httpStatusCode(statusCode.value())
                        .build(), statusCode);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        log.error(ex.getMessage());
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        String fieldMessage = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .reason(fieldMessage)
                        .developerMessage(ex.getMessage())
                        .httpStatus(resolve(statusCode.value()))
                        .httpStatusCode(statusCode.value())
                        .build(), statusCode);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Object> sQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .reason(exception.getMessage().contains("Duplicate entry") ? "El email ya se encuentra registrado" : exception.getMessage())
                        .developerMessage(exception.getMessage())
                        .httpStatus(BAD_REQUEST)
                        .httpStatusCode(BAD_REQUEST.value())
                        .build(), BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> badCredentialsException(BadCredentialsException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .reason(exception.getMessage() + ", Email o contraseña incorrectos")
                        .developerMessage(exception.getMessage())
                        .httpStatus(BAD_REQUEST)
                        .httpStatusCode(BAD_REQUEST.value())
                        .build(), BAD_REQUEST);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<HttpResponse> apiException(ApiException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .reason(exception.getMessage())
                        .developerMessage(exception.getMessage())
                        .httpStatus(BAD_REQUEST)
                        .httpStatusCode(BAD_REQUEST.value())
                        .build(), BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpResponse> accessDeniedException(AccessDeniedException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .reason("Acceso denegado. No tienes permiso para acceder a este recurso")
                        .developerMessage(exception.getMessage())
                        .httpStatus(FORBIDDEN)
                        .httpStatusCode(FORBIDDEN.value())
                        .build(), FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse> exception(Exception exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .reason(exception.getMessage() != null ?
                                (exception.getMessage().contains("Se esperaba 1, se obtuvo 0") ? "Registro no encontrado" : exception.getMessage())
                                : "Un error a ocurrido.")
                        .developerMessage(exception.getMessage())
                        .httpStatus(INTERNAL_SERVER_ERROR)
                        .httpStatusCode(INTERNAL_SERVER_ERROR.value())
                        .build(), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<HttpResponse> emptyResultDataAccessException(EmptyResultDataAccessException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .reason(exception.getMessage().contains("expected 1, actual 0") ? "Record not found" : exception.getMessage())
                        .developerMessage(exception.getMessage())
                        .httpStatus(BAD_REQUEST)
                        .httpStatusCode(BAD_REQUEST.value())
                        .build(), BAD_REQUEST);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<HttpResponse> disabledException(DisabledException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .developerMessage(exception.getMessage())
                        //.reason(exception.getMessage() + ". Por favor, verifica tu correo electrónico para activar tu cuenta.")
                        .reason("La cuenta de usuario se encuentra desactivada. Por favor, verifica tu correo electrónico para activar tu cuenta.")
                        .httpStatus(BAD_REQUEST)
                        .httpStatusCode(BAD_REQUEST.value()).build()
                , BAD_REQUEST);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<HttpResponse> lockedException(LockedException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .developerMessage(exception.getMessage())
                        //.reason(exception.getMessage() + ", too many failed attempts.")
                        .reason("User account is currently locked")
                        .httpStatus(BAD_REQUEST)
                        .httpStatusCode(BAD_REQUEST.value()).build()
                , BAD_REQUEST);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<HttpResponse> dataAccessException(DataAccessException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .reason(processErrorMessage(exception.getMessage()))
                        .developerMessage(processErrorMessage(exception.getMessage()))
                        .httpStatus(BAD_REQUEST)
                        .httpStatusCode(BAD_REQUEST.value()).build()
                , BAD_REQUEST);
    }

    @ExceptionHandler(JWTDecodeException.class)
    public ResponseEntity<HttpResponse> exception(JWTDecodeException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .reason("Could not decode the token")
                        .developerMessage(exception.getMessage())
                        .httpStatus(INTERNAL_SERVER_ERROR)
                        .httpStatusCode(INTERNAL_SERVER_ERROR.value())
                        .build(), INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<HttpResponse> createErrorHttpResponse(HttpStatus httpStatus, String reason, Exception exception) {
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .developerMessage(exception.getMessage())
                        .reason(reason)
                        .httpStatus(httpStatus)
                        .httpStatusCode(httpStatus.value()).build()
                , httpStatus);
    }

    private String processErrorMessage(String errorMessage) {
        if(errorMessage != null) {
            if(errorMessage.contains("Duplicate entry") && errorMessage.contains("AccountVerifications")) {
                return "You already verified your account.";
            }
            if(errorMessage.contains("Duplicate entry") && errorMessage.contains("ResetPasswordVerifications")) {
                return "We already sent you an email to reset your password.";
            }
            if(errorMessage.contains("Duplicate entry")) {
                return "Duplicate entry. Please try again.";
            }
        }
        return "Some error occurred";
    }
}
