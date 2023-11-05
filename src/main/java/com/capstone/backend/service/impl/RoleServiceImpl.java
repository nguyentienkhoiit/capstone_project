package com.capstone.backend.service.impl;

import com.capstone.backend.entity.Role;
import com.capstone.backend.entity.SystemPermission;
import com.capstone.backend.entity.User;
import com.capstone.backend.entity.UserRolePermission;
import com.capstone.backend.exception.ApiException;
import com.capstone.backend.model.dto.role.*;
import com.capstone.backend.model.mapper.RoleMapper;
import com.capstone.backend.model.mapper.SystemPermissionMapper;
import com.capstone.backend.repository.RoleRepository;
import com.capstone.backend.repository.SystemPermissionRepository;
import com.capstone.backend.repository.UserRepository;
import com.capstone.backend.repository.UserRolePermissionRepository;
import com.capstone.backend.repository.criteria.RoleCriteria;
import com.capstone.backend.service.RoleService;
import com.capstone.backend.utils.Constants;
import com.capstone.backend.utils.MessageException;
import com.capstone.backend.utils.UserHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;
    RoleCriteria roleCriteria;
    UserRolePermissionRepository userRolePermissionRepository;
    MessageException messageException;
    UserRepository userRepository;
    UserHelper userHelper;
    SystemPermissionRepository systemPermissionRepository;

    @Override
    public PagingRoleDTOResponse
    viewSearchRole(RoleDTOFilter request) {
        return roleCriteria.viewSearchRole(request);
    }

    @Override
    public RoleDTODetailResponse getRoleById(Long id) {
        List<SystemPermission> permissions = userRolePermissionRepository
                .getListSystemPermissionByRole(id);
        Role role = roleRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_ROLE_NOT_FOUND));
        return RoleMapper
                .toRoleDTODetailResponse(
                        role,
                        permissions,
                        userRepository.findUsernameByUserId(role.getUserId())
                );
    }

    @Override
    public RoleDTODetailResponse createRole(RoleDTORequest request) {
        User userLoggedIn = userHelper.getUserLogin();
        Role role = RoleMapper.toRole(request, userLoggedIn);
        role = roleRepository.save(role);
        List<SystemPermission> permissions = null;
        if (request.getPermission() != null) {
            permissions = saveUserRolePermission(request.getPermission(), role);
        }
        return RoleMapper
                .toRoleDTODetailResponse(
                        role,
                        permissions,
                        userRepository.findUsernameByUserId(role.getUserId())
                );
    }

    @Override
    public RoleDTODetailResponse updateRole(RoleDTOUpdate request) {
        User userLoggedIn = userHelper.getUserLogin();
        Role role = RoleMapper.toRole(request, userLoggedIn);
        role = roleRepository.save(role);
        //list permission present
        List<SystemPermission> permissions = userRolePermissionRepository
                .getListSystemPermissionByRole(request.getRoleId());

        List<SystemPermission> listAdded = request.getPermission().stream()
                .map(permission ->
                        systemPermissionRepository.
                                findById(permission)
                                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_SYSTEM_PERMISSION_NOT_FOUND))
                )
                .filter(permission -> !permissions.contains(permission))
                .toList();
        List<SystemPermission> listDeleted = permissions.stream()
                .filter(permission -> !request.getPermission().contains(permission.getId()))
                .toList();

        //xóa listDelete
        deleteListPermission(listDeleted, role);
        //thêm listAdded
        addListPermission(listAdded, role);

        var permissionAfters = userRolePermissionRepository
                .getListSystemPermissionByRole(request.getRoleId());
        return RoleMapper
                .toRoleDTODetailResponse(
                        role,
                        permissionAfters,
                        userRepository.findUsernameByUserId(role.getUserId())
                );
    }

    @Override
    public Boolean changeStatus(Long id) {
        Role role = roleRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_ROLE_NOT_FOUND));
        role.setActive(!role.getActive());
        roleRepository.save(role);
        return true;
    }

    @Override
    public UserRoleDTOResponse getListRoleUser() {
        var user = userHelper.getUserLogin();
        List<RoleDTODisplay> roleDTODisplays = roleRepository
                .findAllByUserAndActive(user).stream()
                .map(RoleMapper::toRoleDTODisplay)
                .toList();
        return UserRoleDTOResponse.builder()
                .userId(user.getId())
                .avatar(Constants.HOST_SERVER + "/" + user.getAvatar())
                .email(user.getEmail())
                .username(user.getUsername())
                .roleDTODisplays(roleDTODisplays)
                .build();
    }

    private void addListPermission(List<SystemPermission> listAdded, Role role) {
        listAdded.forEach(permission -> {
            SystemPermission systemPermission = systemPermissionRepository
                    .findById(permission.getId())
                    .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_SYSTEM_PERMISSION_NOT_FOUND));
            UserRolePermission userRolePermission = SystemPermissionMapper
                    .toUserRolePermission(systemPermission, role);
            userRolePermission = userRolePermissionRepository.save(userRolePermission);
        });
    }

    private void deleteListPermission(List<SystemPermission> listDeleted, Role role) {
        listDeleted.forEach(permission -> {
            UserRolePermission userRolePermission = userRolePermissionRepository
                    .findAll(permission.getId(), role.getId());
            userRolePermission.setActive(false);
            userRolePermission = userRolePermissionRepository.save(userRolePermission);
        });
    }

    private List<SystemPermission> saveUserRolePermission(List<Long> permission, Role role) {
        return permission.stream()
                .map(p -> {
                    SystemPermission systemPermission = systemPermissionRepository.findById(p).orElseThrow();
                    UserRolePermission userRolePermission = SystemPermissionMapper.toUserRolePermission(systemPermission, role);
                    userRolePermission = userRolePermissionRepository.save(userRolePermission);
                    return systemPermission;
                }).toList();
    }
}
