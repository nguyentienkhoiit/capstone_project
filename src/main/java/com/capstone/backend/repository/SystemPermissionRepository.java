package com.capstone.backend.repository;

import com.capstone.backend.entity.SystemPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SystemPermissionRepository extends JpaRepository<SystemPermission, Long> {

    @Query("select urp.permission from UserRolePermission  urp where urp.role.id = :roleId " +
            " and urp.active = true and urp.role.active = true and urp.permission.active = true")
    public List<SystemPermission> findSystemPermissionByRoleId(Long roleId);
}
