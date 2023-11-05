package com.capstone.backend.service;

import com.capstone.backend.model.dto.systempermission.*;

import java.util.List;

public interface SystemPermissionService {
    public PagingSystemPermissionDTOResponse viewSearchPermission(SystemPermissionDTOFilter request);

    public SystemPermissionDTOResponse getSystemPermissionById(Long id);

    public SystemPermissionDTOResponse createSystemPermission(SystemPermissionDTORequest systemPermissionDTORequest);

    public SystemPermissionDTOResponse updateSystemPermission(SystemPermissionDTOUpdate systemPermissionDTOUpdate);

    public Boolean changeStatus(Boolean active, Long id);

    public List<PermissionDTODisplay> getSystemPermissionsByRole(Long roleId);
}
