package com.serendipity.ecommerce.domain;

import com.serendipity.ecommerce.dto.UsuarioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import static com.serendipity.ecommerce.dtomapper.UsuarioDTOMapper.fromUsuario;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class UsuarioPrincipal implements UserDetails {
    private final Usuario usuario;
    private final Rol rol;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return stream(this.rol.getPermiso().split(",".trim())).map(SimpleGrantedAuthority::new).collect(toList());
    }

    @Override
    public String getPassword() {
        return this.usuario.getPassword();
    }

    @Override
    public String getUsername() {
        return this.usuario.getEmail();
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
        return this.usuario.isEstado();
    }

    public UsuarioDTO getUsuario() {
        return fromUsuario(this.usuario);
    }
}
