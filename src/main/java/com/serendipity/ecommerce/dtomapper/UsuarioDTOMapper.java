package com.serendipity.ecommerce.dtomapper;

import com.serendipity.ecommerce.domain.Rol;
import com.serendipity.ecommerce.domain.Usuario;
import com.serendipity.ecommerce.dto.UsuarioDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

public class UsuarioDTOMapper {
    public static UsuarioDTO fromUsuario(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        BeanUtils.copyProperties(usuario, usuarioDTO);
        return usuarioDTO;
    }

    public static UsuarioDTO fromUsuario(Usuario usuario, Rol rol) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        BeanUtils.copyProperties(usuario, usuarioDTO);
        usuarioDTO.setRol(rol.getNombre());
        usuarioDTO.setPermisos(rol.getPermiso());
        return usuarioDTO;
    }

    public static Usuario toUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        BeanUtils.copyProperties(usuarioDTO, usuario);
        return usuario;
    }
}
