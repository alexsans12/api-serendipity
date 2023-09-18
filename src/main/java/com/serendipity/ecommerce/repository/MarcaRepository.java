package com.serendipity.ecommerce.repository;

import com.serendipity.ecommerce.domain.Marca;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MarcaRepository  extends PagingAndSortingRepository<Marca, Long>, ListCrudRepository<Marca, Long> {
    Marca findByNombre(String nombre);
}
