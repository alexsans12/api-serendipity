package com.serendipity.ecommerce.service.implementation;

import com.serendipity.ecommerce.domain.Categoria;
import com.serendipity.ecommerce.dto.CategoriaHierarchyDTO;
import com.serendipity.ecommerce.dtomapper.CategoriaDTOMapper;
import com.serendipity.ecommerce.repository.CategoriaRepository;
import com.serendipity.ecommerce.repository.ProductoRepository;
import com.serendipity.ecommerce.service.CategoriaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.serendipity.ecommerce.dtomapper.CategoriaDTOMapper.mapToHierarchyDTO;
import static java.time.LocalDateTime.now;
import static org.springframework.data.domain.PageRequest.of;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CategoriaServiceImpl implements CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    @Override
    public Categoria createCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @Override
    public Categoria updateCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @Override
    public Page<Categoria> getCategorias(int page, int size) {
        return categoriaRepository.findAll(of(page, size));
    }

    @Override
    public Iterable<Categoria> getCategorias() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria getCategoriaById(Long id) {
        return categoriaRepository.findById(id).get();
    }

    @Override
    public void addProductoToCategoria(Long productoId, Long categoriaId) {
        Categoria categoria = categoriaRepository.findById(categoriaId).get();
        productoRepository.findById(productoId).ifPresent(producto -> {
            producto.setCategoria(categoria);
            producto.setIdCategoria(categoria.getIdCategoria());
            producto.setFechaModificacion(now());
            productoRepository.save(producto);
        });
    }

    public List<CategoriaHierarchyDTO> getCategoriasConHijas() {
        List<Categoria> categorias = categoriaRepository.findAll();
        List<CategoriaHierarchyDTO> categoriasHierarchy = new ArrayList<>();

        for (Categoria categoria : categorias) {
            if (categoria.getIdCategoriaPadre() == null) {
                // Es una categoría padre
                CategoriaHierarchyDTO categoriaDTO = mapToHierarchyDTO(categoria, categorias);
                categoriasHierarchy.add(categoriaDTO);
            }
        }

        return categoriasHierarchy;
    }
}
