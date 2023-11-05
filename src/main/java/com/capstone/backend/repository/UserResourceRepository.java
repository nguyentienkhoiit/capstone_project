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
}
