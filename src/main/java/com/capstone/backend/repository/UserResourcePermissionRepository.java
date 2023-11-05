package com.capstone.backend.repository;

import com.capstone.backend.entity.Resource;
import com.capstone.backend.entity.User;
import com.capstone.backend.entity.UserResourcePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserResourcePermissionRepository extends JpaRepository<UserResourcePermission, Long> {

    public Optional<UserResourcePermission> findByUserAndResource(User user, Resource resource);

    @Query("select urp from UserResourcePermission urp where urp.user.id != :userId and" +
            " urp.resource.id = :resourceId and urp.permission like '%D%V%' ")
    public List<UserResourcePermission> findByResource(Long resourceId, Long userId);

    @Query("select urp.resource.author from UserResourcePermission urp where urp.resource.id = :resource")
    public User getUserOwnerResource(Long resource);

}
