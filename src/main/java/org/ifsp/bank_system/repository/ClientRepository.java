package org.ifsp.bank_system.repository;

import org.ifsp.bank_system.model.Client;

import java.util.HashSet;
import java.util.Optional;

public class ClientRepository {

    HashSet<Client> clients = new HashSet<>();

    public boolean save(Client client) {
        return clients.add(client);
    }

    public boolean delete(Client client) {
        return clients.remove(client);
    }

    public Optional<Client> findByDocument(String document) {
        return clients.stream()
                .filter(client -> client.getDocument().equals(document))
                .findFirst();
    }
}
