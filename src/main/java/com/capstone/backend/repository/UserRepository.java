package com.capstone.backend.repository;

import com.capstone.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByEmail(String email);

    @Query("select u from User as u where u.email = ?1 or u.username = ?1")
    public Optional<User> findByUsernameOrEmail(String userEmail);

    public Optional<User> findByPhone(String phone);

    @Modifying
    @Query("UPDATE User a " +
            "SET a.active = TRUE WHERE a.email = ?1")
    public void setActiveUser(String email);

    public Optional<User> findByUsername(String username);

    @Query("select u.username from User u where u.id = :id")
    public String findUsernameByUserId(Long id);
}
