package com.serendipity.ecommerce.repository;

import com.serendipity.ecommerce.domain.Marca;
import com.serendipity.ecommerce.dto.MarcaDTO;
import com.serendipity.ecommerce.dto.UsuarioDTO;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.web.multipart.MultipartFile;

public interface MarcaRepository  extends PagingAndSortingRepository<Marca, Long>, ListCrudRepository<Marca, Long> {
    Marca findByNombre(String nombre);
}
