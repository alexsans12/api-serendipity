package com.serendipity.ecommerce.repository;

import com.serendipity.ecommerce.domain.Producto;
import com.serendipity.ecommerce.domain.Usuario;
import com.serendipity.ecommerce.dto.UsuarioDTO;
import com.serendipity.ecommerce.form.UpdateProfileForm;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

public interface UsuarioRepository<T extends Usuario> {
    /* Basic CRUD operations */
    T create(T data);
    T getById(Long id);
    T update(T data);
    Boolean delete(Long id);

    /* More Complex Operations */
    Usuario getUsuarioByEmail(String email);

    void sendVerificationCode(UsuarioDTO usuario);

    Usuario verifyCode(String email, String code);

    void resetPassword(String email);

    T verifyPasswordKey(String key);

    void updatePassword(Long idUsuario, String password, String confirmPassword);

    T verifyAccountKey(String key);
    T updateUsuarioDetails(UpdateProfileForm usuario);

    void updatePassword(Long idUsuario, String currentPassword, String newPassword, String confirmNewPassword);

    void updateRolUsuario(Long idUsuario, String rol);

    void updateAccountSettings(Long idUsuario, Boolean enabled);

    Usuario toggleMfa(String email);

    void updateImage(UsuarioDTO usuarioDTO, MultipartFile image);
}
