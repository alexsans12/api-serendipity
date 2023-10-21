package com.serendipity.ecommerce.resource;

import com.serendipity.ecommerce.domain.HttpResponse;
import com.serendipity.ecommerce.domain.Usuario;
import com.serendipity.ecommerce.dto.UsuarioDTO;
import com.serendipity.ecommerce.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
@Slf4j
public class ClientResource {
    private final ClientService clientService;

    @GetMapping("/list")
    public ResponseEntity<HttpResponse> getClients(@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("clientes", clientService.getAllClients(page.orElse(0), size.orElse(10))))
                        .message("Lista de clientes obtenida correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<HttpResponse> getCliente(@PathVariable Long id) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("cliente", clientService.getClientById(id)))
                        .message("Cliente obtenido correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/stats")
    public ResponseEntity<HttpResponse> getStats() {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("stats", clientService.getStats()))
                        .message("Stats obtenidos correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }

    @PutMapping("/update")
    public ResponseEntity<HttpResponse> updateCliente(@AuthenticationPrincipal UsuarioDTO usuario, @RequestBody @Valid Usuario cliente) {
        cliente.setModificadoPor(usuario.getIdUsuario());
        cliente.setFechaModificacion(now());
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("cliente", clientService.updateClient(cliente)))
                        .message("Cliente actualizado correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<HttpResponse> deleteCliente(@RequestBody Usuario cliente) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .message("Cliente eliminado correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }
}
