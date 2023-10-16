package com.serendipity.ecommerce.dto;

import com.serendipity.ecommerce.domain.ImagenProducto;
import com.serendipity.ecommerce.domain.Review;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductoDTO {
    private Long idProducto;
    private String nombre;
    private String sku;
    private String descripcion;
    private Integer cantidad;
    private BigDecimal precio;
    private BigDecimal descuento;
    private boolean estado;
    // Imagenes
    private List<ImagenProducto> imagenes;
    // Review
    private List<Review> reviews;
    // Categoria
    private Long idCategoria;
    private String nombreCategoria;
    private String nombreCategoriaPadre;
    // Usuario que creo la marca
    private Long creadoPor;
    private String nombreCreadoPor;
    private String apellidoCreadoPor;
    private LocalDateTime fechaCreacion;
    // Usuario que modifico la marca
    private Long modificadoPor;
    private String nombreModificadoPor;
    private String apellidoModificadoPor;
    private LocalDateTime fechaModificacion;
}
