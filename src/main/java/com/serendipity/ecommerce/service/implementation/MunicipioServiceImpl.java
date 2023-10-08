package com.serendipity.ecommerce.service.implementation;

import com.serendipity.ecommerce.domain.Municipio;
import com.serendipity.ecommerce.repository.MunicipioRepository;
import com.serendipity.ecommerce.service.MunicipioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MunicipioServiceImpl implements MunicipioService {
    private final MunicipioRepository municipioRepository;

    @Override
    public Iterable<Municipio> getAllMunicipios() {
        return municipioRepository.findAll();
    }

    @Override
    public Optional<Municipio> getMunicipioById(Long id) {
        return municipioRepository.findById(id);
    }

    @Override
    public Iterable<Municipio> getMunicipiosByIdDepartamento(Long idDepartamento) {
        return municipioRepository.findMunicipiosByIdDepartamento(idDepartamento);
    }

    @Override
    public Municipio createMunicipio(Municipio municipio) {
        return municipioRepository.save(municipio);
    }

    @Override
    public Municipio updateMunicipio(Municipio municipio) {
        return municipioRepository.save(municipio);
    }

    @Override
    public void deleteMunicipio(Long id) {
        municipioRepository.deleteById(id);
    }
}
