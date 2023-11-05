package com.capstone.backend.repository;

import com.capstone.backend.entity.SystemPermission;
import com.capstone.backend.entity.UserRolePermission;
import com.capstone.backend.entity.type.MethodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRolePermissionRepository extends JpaRepository<UserRolePermission, Long> {

    @Query("select urp from UserRolePermission urp where urp.active = true and urp.permission.active = true and urp.role.name = :roleName " +
            "and urp.permission.methodType = :methodType and urp.permission.path = :url")
    public UserRolePermission needCheckPermission(String url, MethodType methodType, String roleName);

    @Query("select urp.permission from UserRolePermission urp where urp.role.id = :id and urp.active = true and urp.permission.active = true")
    public List<SystemPermission> getListSystemPermissionByRole(Long id);

    @Query("select urp from UserRolePermission urp where urp.permission.id = :systemPermissionId and urp.role.id = :roleId")
    public UserRolePermission findAll(Long systemPermissionId, Long roleId);
}
