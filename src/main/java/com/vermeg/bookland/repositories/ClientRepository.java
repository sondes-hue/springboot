package com.vermeg.bookland.repositories;

import com.vermeg.bookland.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

    boolean existsByEmail(String email);
    boolean existsByEmailAndVerification(String email, int verification);
    Client findClientByEmail(String email);
    Client findClientByEmailAndPassword(String email, String password);
}
