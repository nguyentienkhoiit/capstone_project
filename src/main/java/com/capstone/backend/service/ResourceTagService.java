package com.capstone.backend.service;

import com.capstone.backend.model.dto.resourcetag.ResourceTagDTOResponse;
import com.capstone.backend.model.dto.tag.TagDTOResponse;

import java.util.List;

public interface ResourceTagService {
    List<ResourceTagDTOResponse> getAllResourceTagByTableTypeAndID(String tableType, long detailId);
    ResourceTagDTOResponse applyTagToResource(TagDTOResponse tagDTOResponse, String tableType, long rowId);
    ResourceTagDTOResponse disableTagFromResource(long id);
}
