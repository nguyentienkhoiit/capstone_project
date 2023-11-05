package com.capstone.backend.service.impl;

import com.capstone.backend.repository.ResourcePermissionRepository;
import com.capstone.backend.service.ResourcePermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ResourcePermissionServiceImpl implements ResourcePermissionService {
    ResourcePermissionRepository resourcePermissionRepository;
}
