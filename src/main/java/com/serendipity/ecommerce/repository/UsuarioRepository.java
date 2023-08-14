package com.serendipity.ecommerce.repository;

import com.serendipity.ecommerce.domain.Usuario;

import java.util.Collection;

public interface UsuarioRepository<T extends Usuario> {
    /* Basic CRUD operations */
    T create(T data);
    Collection<T> findAll(int page, int pageSize);
    T getById(Long id);
    T update(T data);
    Boolean delete(Long id);

    /* More Complex Operations */
}
