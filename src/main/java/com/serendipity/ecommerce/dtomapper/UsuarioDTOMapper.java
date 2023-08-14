package com.serendipity.ecommerce.dtomapper;

import com.serendipity.ecommerce.domain.Usuario;
import com.serendipity.ecommerce.dto.UsuarioDTO;
import org.springframework.stereotype.Component;

@Component
public class UsuarioDTOMapper {
    public static UsuarioDTO fromUsuario(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        return null;
    }
}
