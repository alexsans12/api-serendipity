package com.serendipity.ecommerce.service;

import com.serendipity.ecommerce.domain.Marca;
import com.serendipity.ecommerce.dto.MarcaDTO;
import org.springframework.data.domain.Page;

public interface MarcaService {
    // Marcas functions
    Marca createMarca(Marca marca);
    Marca updateMarca(Marca marca);
    Page<Marca> getMarcas(int page, int size);
    Iterable<Marca> getMarcas();
    Marca getMarcaById(Long id);
    Marca getMarcaByNombre(String nombre);
    void addProductoToMarca(Long productoId, Long marcaId);
}
