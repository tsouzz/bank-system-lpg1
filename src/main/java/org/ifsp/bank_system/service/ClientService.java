package org.ifsp.bank_system.service;

import org.ifsp.bank_system.model.Address;
import org.ifsp.bank_system.model.Client;
import org.ifsp.bank_system.model.LegalEntityClient;
import org.ifsp.bank_system.model.NaturalPersonClient;
import org.ifsp.bank_system.repository.ClientRepository;

import java.time.OffsetDateTime;

public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client createNaturalPersonClient(String password, Address address, String name, String cpf){

        if(clientRepository.findByDocument(cpf).isPresent()){
            throw new IllegalArgumentException("Cliente já existente");
        }

        OffsetDateTime createdAt = OffsetDateTime.now();

        Client newClient = new NaturalPersonClient(password, address, createdAt, name, cpf);

        clientRepository.save(newClient);

        return newClient;
    }

    public Client createLegalEntityClient(String password, Address address, String legalName, String cnpj){

        if(clientRepository.findByDocument(cnpj).isPresent()){
            throw new IllegalArgumentException("Cliente já existente");
        }

        OffsetDateTime createdAt = OffsetDateTime.now();

        Client newClient = new LegalEntityClient(password, address, createdAt, legalName, cnpj);

        clientRepository.save(newClient);

        return newClient;
    }
}
