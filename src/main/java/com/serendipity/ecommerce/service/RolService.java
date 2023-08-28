package com.serendipity.ecommerce.service;

import com.serendipity.ecommerce.domain.Rol;

import java.util.Collection;

public interface RolService {
    Rol getRolByUsuarioId(Long id);
    Collection<Rol> getRoles();
}
