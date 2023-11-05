package com.capstone.backend.service.impl;

import com.capstone.backend.entity.SystemPermission;
import com.capstone.backend.entity.User;
import com.capstone.backend.exception.ApiException;
import com.capstone.backend.model.dto.systempermission.*;
import com.capstone.backend.model.mapper.SystemPermissionMapper;
import com.capstone.backend.repository.SystemPermissionRepository;
import com.capstone.backend.repository.UserRepository;
import com.capstone.backend.repository.criteria.SystemPermissionCriteria;
import com.capstone.backend.service.SystemPermissionService;
import com.capstone.backend.utils.MessageException;
import com.capstone.backend.utils.UserHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SystemPermissionServiceImpl implements SystemPermissionService {
    SystemPermissionRepository systemPermissionRepository;
    SystemPermissionCriteria systemPermissionCriteria;
    UserRepository userRepository;
    MessageException messageException;
    UserHelper userHelper;

    @Override
    public PagingSystemPermissionDTOResponse viewSearchPermission(SystemPermissionDTOFilter request) {
        return systemPermissionCriteria.viewSearchPermission(request);
    }

    @Override
    public SystemPermissionDTOResponse getSystemPermissionById(Long id) {
        SystemPermission systemPermission = systemPermissionRepository.findById(id)
                .orElseThrow(() ->
                        ApiException
                                .notFoundException(messageException.MSG_SYSTEM_PERMISSION_NOT_FOUND)
                );
        return SystemPermissionMapper
                .toSystemPermissionDTOResponse(
                        systemPermission,
                        userRepository.findUsernameByUserId(systemPermission.getUserId())
                );
    }

    @Override
    public SystemPermissionDTOResponse createSystemPermission(SystemPermissionDTORequest systemPermissionDTORequest) {
        User userLoggedIn = userHelper.getUserLogin();
        SystemPermission systemPermissionRoot = null;
        if (systemPermissionDTORequest.getDependencyPermissionId() != null) {
            systemPermissionRoot = systemPermissionRepository
                    .findById(systemPermissionDTORequest.getDependencyPermissionId())
                    .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_SYSTEM_PERMISSION_NOT_FOUND));
        }
        SystemPermission systemPermission = SystemPermissionMapper
                .toSystemPermission(systemPermissionDTORequest);
        systemPermission.setSystemPermissionRoot(systemPermissionRoot);
        systemPermission.setUserId(userLoggedIn.getId());
        systemPermission = systemPermissionRepository.save(systemPermission);
        return SystemPermissionMapper
                .toSystemPermissionDTOResponse(
                        systemPermission,
                        userRepository.findUsernameByUserId(systemPermission.getUserId()
                        )
                );
    }

    @Override
    public SystemPermissionDTOResponse updateSystemPermission(SystemPermissionDTOUpdate systemPermissionDTOUpdate) {
        User userLoggedIn = userHelper.getUserLogin();
        SystemPermission systemPermissionRoot = null;
        if (systemPermissionDTOUpdate.getDependencyPermissionId() != null) {
            systemPermissionRoot = systemPermissionRepository
                    .findById(systemPermissionDTOUpdate.getDependencyPermissionId())
                    .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_SYSTEM_PERMISSION_NOT_FOUND));
        }

        SystemPermission systemPermissionDb = systemPermissionRepository
                .findById(systemPermissionDTOUpdate.getPermissionId())
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_SYSTEM_PERMISSION_NOT_FOUND));

        SystemPermission systemPermission = SystemPermissionMapper
                .toSystemPermission(systemPermissionDTOUpdate);
        systemPermission.setSystemPermissionRoot(systemPermissionRoot);
        systemPermission.setUserId(userLoggedIn.getId());
        systemPermission.setActive(systemPermissionDb.getActive());
        systemPermission.setCreatedAt(systemPermissionDb.getCreatedAt());

        systemPermission = systemPermissionRepository.save(systemPermission);
        return SystemPermissionMapper
                .toSystemPermissionDTOResponse(
                        systemPermission,
                        userRepository.findUsernameByUserId(systemPermission.getUserId()
                        )
                );
    }

    @Override
    public Boolean changeStatus(Boolean active, Long id) {
        SystemPermission systemPermission = systemPermissionRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_SYSTEM_PERMISSION_NOT_FOUND));
//        active = !active;
        systemPermission.setActive(active);
        systemPermissionRepository.save(systemPermission);
        return true;
    }

    @Override
    public List<PermissionDTODisplay> getSystemPermissionsByRole(Long roleId) {
        List<SystemPermission> systemPermissions = systemPermissionRepository
                .findSystemPermissionByRoleId(roleId);
        return systemPermissions.stream()
                .map(SystemPermissionMapper::toPermissionDTODisplay)
                .toList();
    }
}
