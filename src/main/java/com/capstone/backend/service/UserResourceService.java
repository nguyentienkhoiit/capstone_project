package com.capstone.backend.service;

import com.capstone.backend.model.dto.userresource.*;
import org.springframework.core.io.Resource;

public interface UserResourceService {
    public Boolean actionResource(UserResourceRequest request);

    public PagingUserResourceDTOResponse viewSearchUserResourceSaved(UserResourceSavedOrSharedDTOFilter request);

    public PagingUserResourceDTOResponse viewSearchUserResourceShared(UserResourceSavedOrSharedDTOFilter request);

    public PagingUserResourceDTOResponse viewSearchMyUserResource(MyUserResourceDTOFilter request);

    public Boolean deleteSavedResource(Long id);

    public Boolean  deleteSharedResource(Long id);
}
