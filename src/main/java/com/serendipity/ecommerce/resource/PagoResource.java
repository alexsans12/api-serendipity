package com.serendipity.ecommerce.resource;

import com.serendipity.ecommerce.domain.HttpResponse;
import com.serendipity.ecommerce.domain.Pago;
import com.serendipity.ecommerce.service.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static com.serendipity.ecommerce.enumeration.EstadoPago.PENDIENTE;
import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/pago")
@RequiredArgsConstructor
public class PagoResource {
    private final PagoService pagoService;

    @GetMapping("/get/{id}")
    public ResponseEntity<HttpResponse> getPago(@PathVariable Long id) {
        Pago pago = pagoService.getPagoById(id).orElse(null);
        if (pago != null) {
            return ResponseEntity.ok(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .data(of("pago", pagoService.getPagoById(id)))
                            .message("Pago obtenido correctamente")
                            .httpStatus(OK)
                            .httpStatusCode(OK.value())
                            .build()
            );
        } else {
            return ResponseEntity.badRequest().body(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .message("Pago no encontrado")
                            .httpStatus(OK)
                            .httpStatusCode(OK.value())
                            .build()
            );
        }
    }

    @PostMapping("/create")
    public ResponseEntity<HttpResponse> createPago(@RequestBody Pago pago) {
        pago.setFechaCreacion(now());
        pago.setEstado(PENDIENTE);

        return ResponseEntity.created(URI.create(""))
                .body(HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("pago", pagoService.createPago(pago)))
                        .message("Pago creado correctamente")
                        .httpStatus(CREATED)
                        .httpStatusCode(CREATED.value())
                        .build()
                );
    }

    @PutMapping("/update")
    public ResponseEntity<HttpResponse> updatePago(@RequestBody Pago pago) {
        Pago pagoActual = pagoService.getPagoById(pago.getIdPago()).orElse(null);

        if (pagoActual != null) {
            pagoActual.setEstado(pago.getEstado());

            return ResponseEntity.ok(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .data(of("pago", pagoService.updatePago(pagoActual)))
                            .message("Pago actualizado correctamente")
                            .httpStatus(OK)
                            .httpStatusCode(OK.value())
                            .build()
            );
        } else {
            return ResponseEntity.badRequest().body(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .message("Pago no encontrado")
                            .httpStatus(OK)
                            .httpStatusCode(OK.value())
                            .build()
            );
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<HttpResponse> deletePago(@RequestBody Pago pago) {
        pagoService.deletePago(pago.getIdPago());
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .message("Pago eliminado correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }
}
