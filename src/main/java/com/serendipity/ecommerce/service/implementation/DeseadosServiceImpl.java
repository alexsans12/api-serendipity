package com.serendipity.ecommerce.service.implementation;

import com.serendipity.ecommerce.domain.Deseados;
import com.serendipity.ecommerce.repository.DeseadosRepository;
import com.serendipity.ecommerce.service.DeseadosService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DeseadosServiceImpl implements DeseadosService {
    private final DeseadosRepository deseadosRepository;

    @Override
    public Iterable<Deseados> getAllListaDeseos() {
        return deseadosRepository.findAll();
    }

    @Override
    public Optional<Deseados> getListaDeseosById(Long id) {
        return deseadosRepository.findById(id);
    }

    @Override
    public Optional<Deseados> getListaDeseosByUsuario(Long idUsuario) {
        return deseadosRepository.findByIdUsuario(idUsuario);
    }

    @Override
    public Deseados createListaDeseos(Deseados deseados) {
        return deseadosRepository.save(deseados);
    }

    @Override
    public Deseados updateListaDeseos(Deseados deseados) {
        return deseadosRepository.save(deseados);
    }

    @Override
    public void deleteListaDeseos(Long id) {
        deseadosRepository.deleteById(id);
    }
}
