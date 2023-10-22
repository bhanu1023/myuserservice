package com.example.userservice.Repositories;

import com.example.userservice.Models.Sessions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Sessions, Long> {
    public Optional<Sessions> findByToken(String token);
    public void deleteByToken(String token);
}
