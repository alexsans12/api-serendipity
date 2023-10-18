package com.serendipity.ecommerce.repository;


import com.serendipity.ecommerce.domain.Rol;

import java.util.Collection;

public interface RolRepository<T extends Rol> {
    /* Basic CRUD operations */
    T create(T data);

    Collection<Rol> findAll();

    T getById(Long id);

    T update(T data);

    Boolean delete(Long id);

    /* More Complex Operations */
    void addRolToUsuario(Long usuarioId, String rolNombre);
    Rol getRolByUsuarioId(Long usuarioId);
    Rol getRolByUsuarioEmail(String email);
    void updateRolUsuario(Long usuarioId, String rolNombre);
}
