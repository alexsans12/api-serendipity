package com.serendipity.ecommerce.service;

import com.serendipity.ecommerce.domain.Stats;
import com.serendipity.ecommerce.domain.Usuario;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ClientService {
    Page<Usuario> getAllClients(int page, int size);

    Optional<Usuario> getClientById(Long id);

    Usuario updateClient(Usuario client);

    void deleteClient(Long id);

    Stats getStats();
}
