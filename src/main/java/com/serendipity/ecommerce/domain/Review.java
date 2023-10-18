package com.serendipity.ecommerce.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenerationTime;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id_review")
    private Long idReview;

    @Column(name = "id_producto", nullable = false)
    private Long idProducto;

    @JsonIgnore
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "id_producto",  referencedColumnName = "id_producto", insertable = false, updatable = false)
    private Producto producto;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(nullable = false)
    private String comentario;

    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private Integer rating;

    @Column(name = "fecha_creacion", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaCreacion;
}
