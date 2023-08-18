package com.serendipity.ecommerce.service;

import com.serendipity.ecommerce.domain.Usuario;
import com.serendipity.ecommerce.dto.UsuarioDTO;

public interface UsuarioService {
    UsuarioDTO createUsuario(Usuario usuario);
    UsuarioDTO getUsuarioByEmail(String email);

    void sendVerificationCode(UsuarioDTO usuario);

    UsuarioDTO verifyCode(String email, String code);

    void resetPassword(String email);

    UsuarioDTO verifyPasswordKey(String key);

    void renewPassword(String key, String password, String confirmPassword);

    UsuarioDTO verifyAccountKey(String key);
}
