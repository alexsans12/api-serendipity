package com.serendipity.ecommerce.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Data
@SuperBuilder
@JsonInclude(NON_DEFAULT)
public class HttpResponse {
    protected String timestamp;
    protected int httpStatusCode; // 200, 201, 400, 500, etc
    protected HttpStatus httpStatus; // OK, CREATED, BAD_REQUEST, INTERNAL_SERVER_ERROR, etc
    protected String reason; // "OK", "CREATED", "BAD_REQUEST", "INTERNAL_SERVER_ERROR", etc
    protected String message; // "Usuario creado exitosamente", "Usuario actualizado exitosamente", etc
    protected String developerMessage; // "com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException", etc
    protected Map<?, ?> data;
}
