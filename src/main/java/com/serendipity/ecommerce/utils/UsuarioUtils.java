package com.serendipity.ecommerce.utils;

import com.serendipity.ecommerce.domain.UsuarioPrincipal;
import com.serendipity.ecommerce.dto.UsuarioDTO;
import org.springframework.security.core.Authentication;

public class UsuarioUtils {
    public static UsuarioDTO getAuthenticatedUsuario(Authentication authentication) {
        return ((UsuarioDTO) authentication.getPrincipal());
    }

    public static UsuarioDTO getLoggedInUsuario(Authentication authentication) {
        return ((UsuarioPrincipal) authentication.getPrincipal()).getUsuario();
    }
}