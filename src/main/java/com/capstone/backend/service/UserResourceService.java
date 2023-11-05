package com.capstone.backend.service;

import com.capstone.backend.model.dto.userresource.MyUserResourceDTOFilter;
import com.capstone.backend.model.dto.userresource.PagingUserResourceDTOResponse;
import com.capstone.backend.model.dto.userresource.UserResourceRequest;
import com.capstone.backend.model.dto.userresource.UserResourceSavedOrSharedDTOFilter;
import org.springframework.core.io.Resource;

public interface UserResourceService {
    public Boolean actionResource(UserResourceRequest request);

    public PagingUserResourceDTOResponse viewSearchUserResourceSaved(UserResourceSavedOrSharedDTOFilter request);

    public PagingUserResourceDTOResponse viewSearchUserResourceShared(UserResourceSavedOrSharedDTOFilter request);

    public PagingUserResourceDTOResponse viewSearchMyUserResource(MyUserResourceDTOFilter request);

    public Boolean deleteSavedResource(Long id);

    public Boolean  deleteSharedResource(Long id);
}
