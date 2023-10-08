package com.serendipity.ecommerce.service.implementation;

import com.serendipity.ecommerce.domain.Departamento;
import com.serendipity.ecommerce.repository.DepartamentoRepository;
import com.serendipity.ecommerce.service.DepartamentoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DepartamentoServiceImpl implements DepartamentoService {
    private final DepartamentoRepository departamentoRepository;

    @Override
    public Iterable<Departamento> getAllDepartamentos() {
        return departamentoRepository.findAll();
    }

    @Override
    public Optional<Departamento> getDepartamentoById(Long id) {
        return departamentoRepository.findById(id);
    }

    @Override
    public Departamento createDepartamento(Departamento departamento) {
        return departamentoRepository.save(departamento);
    }

    @Override
    public Departamento updateDepartamento(Departamento departamento) {
        return departamentoRepository.save(departamento);
    }

    @Override
    public void deleteDepartamento(Long id) {
        departamentoRepository.deleteById(id);
    }
}
