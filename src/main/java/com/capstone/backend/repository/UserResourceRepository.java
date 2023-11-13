package com.capstone.backend.repository;

import com.capstone.backend.entity.UserResource;
import com.capstone.backend.entity.type.ActionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserResourceRepository extends JpaRepository<UserResource, Long> {
    @Modifying
    @Transactional
    @Query("delete from UserResource u where u.user.id = :id and u.resource.id = :resourceId and u.actionType = :actionType")
    public void deleteUserResourceHasActionType(Long id, Long resourceId, ActionType actionType);

    @Query("select u from UserResource u where u.user.id = :id and u.resource.id = :resourceId and u.actionType = :actionType")
    public Optional<UserResource> findUserResourceHasActionType(Long id, Long resourceId, ActionType actionType);

    @Query("select count(ur) from UserResource ur where ur.actionType = :actionType and ur.resource.id = :resourceId")
    public Long countByActionTypeWithResource(ActionType actionType, Long resourceId);

//    @Query("select count(ur) from UserResource ur where ur.resource.author.id = :userId " +
//            "and ur.actionType = :actionType and ur.active = true and ur.resource.active = true")
//    public Long countUserResourceByType(Long userId, ActionType actionType);

    @Query("select count(ur) from UserResource ur where ur.user.id = :userId and " +
            "ur.actionType = :actionType and ur.active = true and ur.resource.active = true")
    public Long countUserResourceByType(Long userId, ActionType actionType);

    @Query("select count(r) from Resource r where r.author.id = :userId and r.active = true")
    public Long countResourceUploadedByUser(Long userId);
}
