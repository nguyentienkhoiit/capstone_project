package com.capstone.backend.service.impl;

import com.capstone.backend.entity.Resource;
import com.capstone.backend.entity.User;
import com.capstone.backend.entity.UserResource;
import com.capstone.backend.entity.UserResourcePermission;
import com.capstone.backend.entity.type.ActionType;
import com.capstone.backend.exception.ApiException;
import com.capstone.backend.model.dto.userresource.*;
import com.capstone.backend.repository.ResourceRepository;
import com.capstone.backend.repository.UserResourcePermissionRepository;
import com.capstone.backend.repository.UserResourceRepository;
import com.capstone.backend.repository.criteria.UserResourceCriteria;
import com.capstone.backend.service.UserResourceService;
import com.capstone.backend.utils.MessageException;
import com.capstone.backend.utils.UserHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserResourceServiceImpl implements UserResourceService {
    ResourceRepository resourceRepository;
    UserResourceRepository userResourceRepository;
    UserHelper userHelper;
    MessageException messageException;
    UserResourceCriteria userResourceCriteria;
    UserResourcePermissionRepository userResourcePermissionRepository;

    private Boolean findUserResourceHasActionType(User userLoggedIn, Long resourceId, ActionType actionType) {
        Optional<?> optional = userResourceRepository.findUserResourceHasActionType(
                userLoggedIn.getId(),
                resourceId,
                actionType
        );
        return optional.isPresent();
    }

    private void findAndDeleteUserResourceExist(User userLoggedIn, Long resourceId, ActionType actionType) {
        Optional<UserResource> optional = userResourceRepository.findUserResourceHasActionType(
                userLoggedIn.getId(),
                resourceId,
                actionType
        );

        if (optional.isPresent()) {
            userResourceRepository.deleteUserResourceHasActionType(userLoggedIn.getId(), resourceId, actionType);
        }
    }

    @Override
    public Boolean actionResource(UserResourceRequest request) {
        User userLoggedIn = userHelper.getUserLogin();
        Resource resource = resourceRepository.findById(request.getResourceId())
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_RESOURCE_NOT_FOUND));
        UserResource userResource = UserResource.builder()
                .user(userHelper.getUserLogin())
                .actionType(request.getActionType())
                .createdAt(LocalDateTime.now())
                .resource(resource)
                .active(true)
                .build();

        if (request.getActionType() == ActionType.LIKE) {
            findAndDeleteUserResourceExist(userLoggedIn, request.getResourceId(), ActionType.UNLIKE);
            if (!findUserResourceHasActionType(userLoggedIn, request.getResourceId(), ActionType.LIKE)) {
                userResource = userResourceRepository.save(userResource);
            }
        } else if (request.getActionType() == ActionType.UNLIKE) {
            findAndDeleteUserResourceExist(userLoggedIn, request.getResourceId(), ActionType.LIKE);
            if (!findUserResourceHasActionType(userLoggedIn, request.getResourceId(), ActionType.UNLIKE)) {
                userResource = userResourceRepository.save(userResource);
            }
        } else if (request.getActionType() == ActionType.SAVED) {
            findAndDeleteUserResourceExist(userLoggedIn, request.getResourceId(), ActionType.UNSAVED);
            if (!findUserResourceHasActionType(userLoggedIn, request.getResourceId(), ActionType.SAVED)) {
                userResource = userResourceRepository.save(userResource);
            }
        } else if (request.getActionType() == ActionType.UNSAVED) {
            findAndDeleteUserResourceExist(userLoggedIn, request.getResourceId(), ActionType.SAVED);
        } else if (request.getActionType() == ActionType.DOWNLOAD) {
            userResource = userResourceRepository.save(userResource);
        }
        return true;
    }

    @Override
    public PagingUserResourceDTOResponse viewSearchUserResourceSaved(UserResourceSavedOrSharedDTOFilter request) {
        return userResourceCriteria.viewSearchUserSavedResource(request);
    }

    @Override
    public PagingUserResourceDTOResponse viewSearchUserResourceShared(UserResourceSavedOrSharedDTOFilter request) {
        return userResourceCriteria.viewSearchUserResourceShared(request);
    }

    @Override
    public PagingUserResourceDTOResponse viewSearchMyUserResource(MyUserResourceDTOFilter request) {
        return userResourceCriteria.viewSearchMyUserResource(request);
    }

    @Override
    public Boolean deleteSavedResource(Long id) {
        User user = userHelper.getUserLogin();
        UserResource userResource = userResourceRepository
                .findUserResourceHasActionType(user.getId(), id, ActionType.SAVED)
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_USER_RESOURCE_NOT_FOUND));
        userResourceRepository.deleteUserResourceHasActionType(user.getId(), id, ActionType.SAVED);
        return true;
    }

    @Override
    public Boolean deleteSharedResource(Long resourceId) {
        User user = userHelper.getUserLogin();
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_RESOURCE_NOT_FOUND));
        UserResourcePermission userResourcePermission = userResourcePermissionRepository
                .findByUserAndResourceAndPermission(user, resource)
                .orElseThrow(() -> ApiException.badRequestException("user resource permission is not found"));
        userResourcePermissionRepository.delete(userResourcePermission);
        return true;
    }
}
