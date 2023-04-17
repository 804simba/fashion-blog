package com.timolisa.fashionblogapi.repository;

import com.timolisa.fashionblogapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByUsernameAndPassword(String username, String password);
    Optional<User> findByUsernameAndPassword(String username, String password);
}
