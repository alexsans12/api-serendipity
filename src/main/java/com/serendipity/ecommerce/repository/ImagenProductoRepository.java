package com.serendipity.ecommerce.repository;

import com.serendipity.ecommerce.domain.ImagenProducto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImagenProductoRepository extends CrudRepository<ImagenProducto, Long> {
    Optional<ImagenProducto> getImagenProductoByUrl(String url);
    List<ImagenProducto> findByIdProducto(Long idProducto);
    void deleteByIdProducto(Long idProducto);
}
