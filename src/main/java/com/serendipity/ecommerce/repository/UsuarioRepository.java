package com.serendipity.ecommerce.repository;

import com.serendipity.ecommerce.domain.Usuario;
import com.serendipity.ecommerce.dto.UsuarioDTO;
import com.serendipity.ecommerce.form.UpdateProfileForm;

import java.util.Collection;

public interface UsuarioRepository<T extends Usuario> {
    /* Basic CRUD operations */
    T create(T data);
    Collection<T> findAll(int page, int pageSize);
    T getById(Long id);
    T update(T data);
    Boolean delete(Long id);

    /* More Complex Operations */
    Usuario getUsuarioByEmail(String email);

    void sendVerificationCode(UsuarioDTO usuario);

    Usuario verifyCode(String email, String code);

    void resetPassword(String email);

    T verifyPasswordKey(String key);

    void renewPassword(String key, String password, String confirmPassword);

    T verifyAccountKey(String key);
    T updateUsuarioDetails(UpdateProfileForm usuario);
}
