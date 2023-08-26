package com.serendipity.ecommerce.service.implementation;

import com.serendipity.ecommerce.domain.Rol;
import com.serendipity.ecommerce.domain.Usuario;
import com.serendipity.ecommerce.dto.UsuarioDTO;
import com.serendipity.ecommerce.form.UpdateProfileForm;
import com.serendipity.ecommerce.repository.RolRepository;
import com.serendipity.ecommerce.repository.UsuarioRepository;
import com.serendipity.ecommerce.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public void renewPassword(String key, String password, String confirmPassword) {
        usuarioRepository.renewPassword(key, password, confirmPassword);
    }

    @Override
    public UsuarioDTO verifyAccountKey(String key) {
        return mapToUsuarioDTO(usuarioRepository.verifyAccountKey(key));
    }

    @Override
    public UsuarioDTO updateUsuarioDetails(UpdateProfileForm usuario) {
        return mapToUsuarioDTO(usuarioRepository.updateUsuarioDetails(usuario));
    }

    private UsuarioDTO mapToUsuarioDTO(Usuario usuario) {
        return fromUsuario(usuario, rolRepository.getRolByUsuarioId(usuario.getIdUsuario()));
    }
}
