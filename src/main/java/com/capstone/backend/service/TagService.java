package com.capstone.backend.service;

import com.capstone.backend.entity.Tag;
import com.capstone.backend.model.dto.tag.PagingTagDTOResponse;
import com.capstone.backend.model.dto.tag.TagDTOFilter;
import com.capstone.backend.model.dto.tag.TagDTORequest;
import com.capstone.backend.model.dto.tag.TagDTOResponse;

public interface TagService {
    public Tag saveTag(Tag tag);
    public TagDTOResponse createTag(TagDTORequest tagDTORequest);
    public TagDTOResponse disableTag(long id);
    public TagDTOResponse getTagByID(long id);
    public PagingTagDTOResponse searchTags(TagDTOFilter tagDTOFilter);
}
