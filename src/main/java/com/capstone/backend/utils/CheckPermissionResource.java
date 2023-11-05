package com.capstone.backend.utils;

import com.capstone.backend.entity.Resource;
import com.capstone.backend.entity.User;
import com.capstone.backend.entity.type.ApproveType;
import com.capstone.backend.entity.type.PermissionResourceType;
import com.capstone.backend.entity.type.VisualType;
import com.capstone.backend.exception.ApiException;
import com.capstone.backend.repository.UserResourcePermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CheckPermissionResource {
    UserResourcePermissionRepository userResourcePermissionRepository;
    MessageException messageException;

    public Boolean checkPermissionResourceType(
            User user,
            Resource resource,
            PermissionResourceType permissionResourceType
    ) {
        var permission = userResourcePermissionRepository.findByUserAndResource(user, resource)
                .orElseThrow(() -> ApiException.unAuthorizedException(messageException.MSG_NO_PERMISSION));
        return permission.getPermission().contains(permissionResourceType.toString());
    }

    public Boolean needCheckPermissionResource(
            User user,
            Resource resource,
            PermissionResourceType permissionResourceType
    ) {
        if (user != null) {
            if(user == resource.getAuthor()) {
                return checkPermissionResourceType(user, resource, permissionResourceType);
            }
            else if (resource.getApproveType() == ApproveType.ACCEPTED) {
                if (resource.getVisualType() != VisualType.PUBLIC) {
                    if (resource.getVisualType() == VisualType.RESTRICT) {
                        return checkPermissionResourceType(user, resource, permissionResourceType);
                    } else return false;
                }
                else return true;
            } else return false;
        } else return resource.getVisualType() == VisualType.PUBLIC && resource.getApproveType() == ApproveType.ACCEPTED;
    }
}
