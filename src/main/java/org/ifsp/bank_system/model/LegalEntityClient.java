package org.ifsp.bank_system.model;

import java.time.OffsetDateTime;
import java.util.Objects;

public final class LegalEntityClient extends Client {

    private String legalName;
    private String cnpj;

    public LegalEntityClient(String password, Address address, OffsetDateTime createdAt, String legalName, String cnpj) {
        super(password, address, createdAt);
        this.legalName = legalName;
        this.cnpj = cnpj;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getCnpj() {
        return cnpj;
    }

    @Override
    public String getDocument() {
        return this.cnpj;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LegalEntityClient that = (LegalEntityClient) o;
        return Objects.equals(cnpj, that.cnpj);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cnpj);
    }
}
