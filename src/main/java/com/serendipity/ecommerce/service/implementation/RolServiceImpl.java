package com.serendipity.ecommerce.service.implementation;

import com.serendipity.ecommerce.domain.Rol;
import com.serendipity.ecommerce.repository.RolRepository;
import com.serendipity.ecommerce.service.RolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService {
    private final RolRepository rolRepository;
    @Override
    public Rol getRolByUsuarioId(Long id) {
        return rolRepository.getRolByUsuarioId(id);
    }
}
