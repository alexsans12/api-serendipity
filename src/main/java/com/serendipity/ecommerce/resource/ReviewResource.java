package com.serendipity.ecommerce.resource;

import com.serendipity.ecommerce.domain.HttpResponse;
import com.serendipity.ecommerce.domain.Review;
import com.serendipity.ecommerce.dto.UsuarioDTO;
import com.serendipity.ecommerce.form.ReviewForm;
import com.serendipity.ecommerce.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewResource {
    private final ReviewService reviewService;

    @GetMapping("/get-all")
    public ResponseEntity<HttpResponse> getReviews() {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("reviews", reviewService.getAllReviews()))
                        .message("Lista de reviews obtenida correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<HttpResponse> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("review", reviewService.getReviewById(id)))
                        .message("Review obtenida correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/get-reviews/{id}")
    public ResponseEntity<HttpResponse> getReviewsByIdProducto(@PathVariable Long id) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("reviews", reviewService.getReviewsByProductId(id)))
                        .message("Lista de reviews obtenida correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }

    @PostMapping("/create")
    public ResponseEntity<HttpResponse> createReview(@AuthenticationPrincipal UsuarioDTO usuario, @RequestBody @Valid ReviewForm form) {
        Review review = new Review();
        review.setComentario(form.getComentario());
        review.setRating(form.getPuntuacion());
        review.setIdProducto(form.getIdProducto());
        review.setIdUsuario(usuario.getIdUsuario());
        review.setFechaCreacion(now());

        reviewService.createReview(review);

        return ResponseEntity.created(URI.create(""))
                .body(HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("reviews", reviewService.getReviewsByProductId(form.getIdProducto())))
                        .message("Lista de reviews obtenida correctamente")
                        .httpStatus(CREATED)
                        .httpStatusCode(CREATED.value())
                        .build()
                );
    }

    @PutMapping("/update")
    public ResponseEntity<HttpResponse> updateReview(@RequestBody @Valid ReviewForm form) {
        Review review = reviewService.getReviewById(form.getIdReview()).orElse(null);

        if (review != null) {
            review.setComentario(form.getComentario());
            review.setRating(form.getPuntuacion());

            reviewService.updateReview(review);

            return ResponseEntity.ok(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .data(of("reviews", reviewService.getReviewsByProductId(form.getIdProducto())))
                            .message("Lista de reviews obtenida correctamente")
                            .httpStatus(OK)
                            .httpStatusCode(OK.value())
                            .build()
            );
        } else {
            return ResponseEntity.badRequest().body(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .message("Review no encontrada")
                            .httpStatus(OK)
                            .httpStatusCode(OK.value())
                            .build()
            );
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<HttpResponse> deleteDireccion(@RequestBody Review review) {
        reviewService.deleteReview(review.getIdReview());
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("reviews", reviewService.getReviewsByProductId(review.getIdProducto())))
                        .message("Lista de reviews obtenida correctamente")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }
}
