package com.serendipity.ecommerce.service;

import com.serendipity.ecommerce.domain.Usuario;
import com.serendipity.ecommerce.dto.UsuarioDTO;
import com.serendipity.ecommerce.form.UpdateProfileForm;
import org.springframework.web.multipart.MultipartFile;

public interface UsuarioService {
    UsuarioDTO getUsuarioById(Long id);
    UsuarioDTO createUsuario(Usuario usuario);
    UsuarioDTO getUsuarioByEmail(String email);

    void sendVerificationCode(UsuarioDTO usuario);

    UsuarioDTO verifyCode(String email, String code);

    void resetPassword(String email);

    UsuarioDTO verifyPasswordKey(String key);

    void updatePassword(Long idUsuario, String password, String confirmPassword);

    UsuarioDTO verifyAccountKey(String key);
    UsuarioDTO updateUsuarioDetails(UpdateProfileForm usuario);

    void updatePassword(Long idUsuario, String currentPassword, String newPassword, String confirmNewPassword);

    void updateRolUsuario(Long idUsuario, String rol);

    void updateAccountSettings(Long idUsuario, Boolean enabled);

    UsuarioDTO toggleMfa(String email);

    void updateImage(UsuarioDTO usuarioDTO, MultipartFile image);
}
