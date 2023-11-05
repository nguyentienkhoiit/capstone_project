package com.capstone.backend.model.mapper;

import com.capstone.backend.entity.Role;
import com.capstone.backend.entity.SystemPermission;
import com.capstone.backend.entity.User;
import com.capstone.backend.model.dto.role.*;
import com.capstone.backend.model.dto.systempermission.PermissionDTOResponse;

import java.time.LocalDateTime;
import java.util.List;

public class RoleMapper {
    public static RoleDTOResponse toRoleDTOResponse(Role role, String creator) {
        return RoleDTOResponse.builder()
                .roleId(role.getId())
                .roleName(role.getName())
                .active(role.getActive())
                .createdAt(role.getCreatedAt())
                .description(role.getDescription())
                .creator(creator)
                .build();
    }

    public static RoleDTODetailResponse toRoleDTODetailResponse(Role role, List<SystemPermission> permissions, String creator) {
        List<PermissionDTOResponse> permissionDTOResponseList = null;
        if(permissions != null) {
            permissionDTOResponseList = permissions.stream()
                    .map(SystemPermissionMapper::toPermissionDTOResponse)
                    .toList();
        }
        return RoleDTODetailResponse.builder()
                .roleId(role.getId())
                .roleName(role.getName())
                .active(role.getActive())
                .createdAt(role.getCreatedAt())
                .description(role.getDescription())
                .creator(creator)
                .permissions(permissionDTOResponseList)
                .build();
    }

    public static Role toRole(RoleDTORequest request, User userLoggedIn) {
        return Role.builder()
                .active(true)
                .createdAt(LocalDateTime.now())
                .name(request.getRoleName())
                .description(request.getDescription())
                .userId(userLoggedIn.getId())
                .build();
    }

    public static Role toRole(RoleDTOUpdate request, User userLoggedIn) {
        return Role.builder()
                .id(request.getRoleId())
                .active(true)
                .createdAt(LocalDateTime.now())
                .name(request.getRoleName())
                .description(request.getDescription())
                .userId(userLoggedIn.getId())
                .build();
    }

    public static RoleDTODisplay toRoleDTODisplay(Role role) {
        return RoleDTODisplay.builder()
                .roleId(role.getId())
                .roleName(role.getName())
                .build();
    }
}
