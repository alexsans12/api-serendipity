package com.serendipity.ecommerce.service;

import com.serendipity.ecommerce.domain.ImagenProducto;

import java.util.List;

public interface ImagenProductoService {
    ImagenProducto createImagenProducto(ImagenProducto imagenProducto);
    ImagenProducto getImagenProductoById(Long id);
    List<ImagenProducto> getImagenProductoByIdProducto(Long idProducto);
    Long getImagenProductoByUrl(String url);
    void deleteImagenProductoByIdProducto(Long idProducto);
}
