package org.ifsp.bank_system.model;

import java.time.OffsetDateTime;

public abstract class Client {

    private Address address;
    private OffsetDateTime createdAt;
    private String password;
    private boolean isVip = false;

    public Client(String password, Address address, OffsetDateTime createdAt) {
        this.address = address;
        this.createdAt = createdAt;
        this.password = password;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public abstract String getDocument();

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }
}
