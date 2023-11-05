package com.capstone.backend.service.impl;

import com.capstone.backend.repository.UserResourceRepository;
import com.capstone.backend.service.UserResourcePermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserResourcePermissionImpl implements UserResourcePermissionService {
    UserResourceRepository userResourceRepository;


}
