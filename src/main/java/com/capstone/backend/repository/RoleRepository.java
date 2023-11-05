package com.capstone.backend.repository;

import com.capstone.backend.entity.Role;
import com.capstone.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("select ur.role from UserRole ur where ur.user = :user and ur.active = true and ur.role.active = true")
    public List<Role> findAllByUserAndActive(User user);
}
