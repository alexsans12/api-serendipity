package com.serendipity.ecommerce.repository;

import com.serendipity.ecommerce.domain.Usuario;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ClientRepository extends PagingAndSortingRepository<Usuario, Long>, ListCrudRepository<Usuario, Long> {
}
