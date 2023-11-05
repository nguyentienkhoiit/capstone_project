package com.capstone.backend.repository;

import com.capstone.backend.entity.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ConfirmTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    @Modifying
    @Query("UPDATE ConfirmationToken c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")
    void updateConfirmedAt(String token,
                           LocalDateTime confirmedAt);

    public Optional<ConfirmationToken> findByToken(String token);
}
