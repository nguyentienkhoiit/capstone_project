package com.capstone.backend.repository;

import com.capstone.backend.entity.User;
import com.capstone.backend.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    @Query("select ur.user from UserRole ur where ur.role.id = 1 and " +
            " ( ur.user.username like %:text% or ur.user.email like %:text% ) and ur.user.id != :userId")
    public List<User> findTeacherByUsernameOrEmailContaining(String text, Long userId);

}
