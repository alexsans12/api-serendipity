package com.serendipity.ecommerce.service.implementation;

import com.serendipity.ecommerce.domain.Rol;
import com.serendipity.ecommerce.domain.Usuario;
import com.serendipity.ecommerce.dto.UsuarioDTO;
import com.serendipity.ecommerce.form.UpdateProfileForm;
import com.serendipity.ecommerce.repository.RolRepository;
import com.serendipity.ecommerce.repository.UsuarioRepository;
import com.serendipity.ecommerce.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.serendipity.ecommerce.dtomapper.UsuarioDTOMapper.fromUsuario;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository<Usuario> usuarioRepository;
    private final RolRepository<Rol> rolRepository;

    @Override
    public UsuarioDTO getUsuarioById(Long id) {
        return mapToUsuarioDTO(usuarioRepository.getById(id));
    }

    @Override
    public UsuarioDTO createUsuario(Usuario usuario) {
        return mapToUsuarioDTO(usuarioRepository.create(usuario));
    }

    @Override
    public UsuarioDTO getUsuarioByEmail(String email) {
        return mapToUsuarioDTO(usuarioRepository.getUsuarioByEmail(email));
    }

    @Override
    public void sendVerificationCode(UsuarioDTO usuario) {
        usuarioRepository.sendVerificationCode(usuario);
    }

    @Override
    public UsuarioDTO verifyCode(String email, String code) {
        return mapToUsuarioDTO(usuarioRepository.verifyCode(email, code));
    }

    @Override
    public void resetPassword(String email) {
        usuarioRepository.resetPassword(email);
    }

    @Override
    public UsuarioDTO verifyPasswordKey(String key) {
        return mapToUsuarioDTO(usuarioRepository.verifyPasswordKey(key));
    }

    @Override
    public void updatePassword(Long idUsuario, String password, String confirmPassword) {
        usuarioRepository.updatePassword(idUsuario, password, confirmPassword);
    }

    @Override
    public UsuarioDTO verifyAccountKey(String key) {
        return mapToUsuarioDTO(usuarioRepository.verifyAccountKey(key));
    }

    @Override
    public UsuarioDTO updateUsuarioDetails(UpdateProfileForm usuario) {
        return mapToUsuarioDTO(usuarioRepository.updateUsuarioDetails(usuario));
    }

    @Override
    public void updatePassword(Long idUsuario, String currentPassword, String newPassword, String confirmNewPassword) {
        usuarioRepository.updatePassword(idUsuario, currentPassword, newPassword, confirmNewPassword);
    }

    @Override
    public void updateRolUsuario(Long idUsuario, String rol) {
        usuarioRepository.updateRolUsuario(idUsuario, rol);
    }

    @Override
    public void updateAccountSettings(Long idUsuario, Boolean enabled) {
        usuarioRepository.updateAccountSettings(idUsuario, enabled);
    }

    @Override
    public UsuarioDTO toggleMfa(String email) {
        return mapToUsuarioDTO(usuarioRepository.toggleMfa(email));
    }

    @Override
    public void updateImage(UsuarioDTO usuarioDTO, MultipartFile image) {
        usuarioRepository.updateImage(usuarioDTO, image);
    }

    private UsuarioDTO mapToUsuarioDTO(Usuario usuario) {
        return fromUsuario(usuario, rolRepository.getRolByUsuarioId(usuario.getIdUsuario()));
    }
}
