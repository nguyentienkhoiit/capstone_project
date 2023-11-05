package com.capstone.backend.model.mapper;

import com.capstone.backend.entity.Resource;
import com.capstone.backend.entity.User;
import com.capstone.backend.entity.UserResourcePermission;
import com.capstone.backend.model.dto.userresource.UserResourceDTOResponse;
import com.capstone.backend.utils.Constants;

import java.time.LocalDateTime;

public class UserResourceMapper {

    public static UserResourceDTOResponse toUserResourceDTOResponse(Resource resource, String username) {
        return UserResourceDTOResponse.builder()
                .resourceName(resource.getName())
                .resourceId(resource.getId())
                .approveType(resource.getApproveType())
                .createdAt(resource.getCreatedAt())
                .visualType(resource.getVisualType())
                .username(username)
                .resourceType(resource.getResourceType())
                .isShare(false)
                .isUpdate(false)
                .isDelete(false)
                .build();
    }

    public static UserResourcePermission toUserResourcePermission(User user, Resource resource) {
        return UserResourcePermission.builder()
                .user(user)
                .resource(resource)
                .active(true)
                .createdAt(LocalDateTime.now())
                .permission(Constants.SHARED_RESOURCE_PERMISSION)
                .build();
    }
}
