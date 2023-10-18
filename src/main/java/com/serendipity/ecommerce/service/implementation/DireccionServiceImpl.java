package com.serendipity.ecommerce.service.implementation;

import com.serendipity.ecommerce.domain.Direccion;
import com.serendipity.ecommerce.repository.DireccionRepository;
import com.serendipity.ecommerce.service.DireccionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DireccionServiceImpl implements DireccionService {
    private final DireccionRepository direccionRepository;

    @Override
    public Iterable<Direccion> getAllDirecciones() {
        return direccionRepository.findAll();
    }

    @Override
    public Iterable<Direccion> getAllDireccionesByUsuario(Long idUsuario) {
        return direccionRepository.findAllByIdUsuario(idUsuario);
    }

    @Override
    public Optional<Direccion> getDireccionById(Long id) {
        return direccionRepository.findById(id);
    }

    @Override
    public Direccion createDireccion(Direccion direccion) {
        return direccionRepository.save(direccion);
    }

    @Override
    public Direccion updateDireccion(Direccion direccion) {
        return direccionRepository.save(direccion);
    }

    @Override
    public void deleteDireccion(Long id) {
        direccionRepository.deleteById(id);
    }
}
