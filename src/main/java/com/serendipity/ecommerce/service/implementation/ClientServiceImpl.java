package com.serendipity.ecommerce.service.implementation;

import com.serendipity.ecommerce.domain.Stats;
import com.serendipity.ecommerce.domain.Usuario;
import com.serendipity.ecommerce.repository.ClientRepository;
import com.serendipity.ecommerce.rowmapper.StatsRowMapper;
import com.serendipity.ecommerce.service.ClientService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static com.serendipity.ecommerce.query.ClientQuery.STATS_QUERY;
import static org.springframework.data.domain.PageRequest.of;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public Page<Usuario> getAllClients(int page, int size) {
        return clientRepository.findAll(of(page, size));
    }

    @Override
    public Optional<Usuario> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    @Override
    public Usuario updateClient(Usuario client) {
        return clientRepository.save(client);
    }

    @Override
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    @Override
    public Stats getStats() {
        return jdbc.queryForObject(STATS_QUERY, Map.of(), new StatsRowMapper());
    }
}
