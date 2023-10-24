package com.serendipity.ecommerce.repository;

import com.serendipity.ecommerce.domain.Categoria;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CategoriaRepository extends PagingAndSortingRepository<Categoria, Long>, ListCrudRepository<Categoria, Long> {
    Categoria findByNombre(String nombre);
}
