package org.ifsp.bank_system.service;

import org.ifsp.bank_system.model.Client;

import java.util.Optional;

public class SessionManager {

    private static Client loggedInClient;

    public static void login(Client client){
        loggedInClient = client;
    }

    public static void logout(){
        loggedInClient = null;
    }

    public static Optional<Client> getLoggedInClient(){
        return Optional.ofNullable(loggedInClient);
    }

    public static boolean isUserLoggedIn(){
        return loggedInClient != null;
    }

}
