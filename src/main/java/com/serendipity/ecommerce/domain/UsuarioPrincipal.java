package com.serendipity.ecommerce.domain;

import com.serendipity.ecommerce.dto.UsuarioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import static com.serendipity.ecommerce.dtomapper.UsuarioDTOMapper.fromUsuario;

@RequiredArgsConstructor
public class UsuarioPrincipal implements UserDetails {
    private final Usuario usuario;
    private final Rol rol;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //return stream(rol.getPermiso().split(",".trim())).map(SimpleGrantedAuthority::new).collect(toList());
        return AuthorityUtils.commaSeparatedStringToAuthorityList(rol.getPermiso());
    }

    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    @Override
    public String getUsername() {
        return usuario.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return usuario.isEstado();
    }

    public UsuarioDTO getUsuario() {
        return fromUsuario(usuario, this.rol);
    }
}
