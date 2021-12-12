package com.vermeg.bookland.repositories;

import com.vermeg.bookland.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findClientByEmail(String email);
}
