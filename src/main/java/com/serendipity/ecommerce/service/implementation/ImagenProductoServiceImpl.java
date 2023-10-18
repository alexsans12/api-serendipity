package com.serendipity.ecommerce.service.implementation;

import com.serendipity.ecommerce.domain.ImagenProducto;
import com.serendipity.ecommerce.repository.ImagenProductoRepository;
import com.serendipity.ecommerce.service.ImagenProductoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ImagenProductoServiceImpl implements ImagenProductoService {
    private final ImagenProductoRepository imagenProductoRepository;

    @Override
    public ImagenProducto createImagenProducto(ImagenProducto imagenProducto) {
        return imagenProductoRepository.save(imagenProducto);
    }

    @Override
    public ImagenProducto getImagenProductoById(Long id) {
        return imagenProductoRepository.findById(id).get();
    }

    @Override
    public List<ImagenProducto> getImagenProductoByIdProducto(Long idProducto) {
        return imagenProductoRepository.findByIdProducto(idProducto);
    }

    @Override
    public Long getImagenProductoByUrl(String url) {
        return imagenProductoRepository.getImagenProductoByUrl(url).orElse(new ImagenProducto()).getIdImagenProducto();
    }

    @Override
    public void deleteImagenProductoByIdProducto(Long idProducto) {
        imagenProductoRepository.deleteByIdProducto(idProducto);
    }
}
