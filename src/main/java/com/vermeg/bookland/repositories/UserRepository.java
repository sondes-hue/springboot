package com.vermeg.bookland.repositories;

import com.vermeg.bookland.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    boolean existsByEmailAndVerification(String email, int verification);
    User findUserByEmail(String email);
    User findUserByEmailAndPassword(String email, String password);

}
