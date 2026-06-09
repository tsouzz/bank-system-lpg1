package org.ifsp.bank_system.model;

import java.time.OffsetDateTime;
import java.util.Objects;

public final class NaturalPersonClient extends Client {

    private String name;
    private String cpf;

    public NaturalPersonClient(String password, Address address, OffsetDateTime createdAt, String name, String cpf) {
        super(password, address, createdAt);
        this.name = name;
        this.cpf = cpf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    @Override
    public String getDocument() {
        return this.cpf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NaturalPersonClient that = (NaturalPersonClient) o;
        return Objects.equals(cpf, that.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cpf);
    }
}
