package com.capstone.backend.model.mapper;

import com.capstone.backend.entity.Resource;
import com.capstone.backend.entity.ResourceTag;
import com.capstone.backend.entity.Tag;
import com.capstone.backend.entity.type.TableType;
import com.capstone.backend.model.dto.resourcetag.ResourceTagDTOResponse;
import com.capstone.backend.model.dto.tag.TagDTOResponse;

import java.time.LocalDateTime;

public class ResourceTagMapper {
    public static ResourceTagDTOResponse toResourceTagDTOResponse(ResourceTag resourceTag) {
        TagDTOResponse tag = TagDTOResponse.builder()
                .id(resourceTag.getTag().getId())
                .name(resourceTag.getTag().getName())
                .build();
        return ResourceTagDTOResponse.builder()
                .id(resourceTag.getId())
                .detailId(resourceTag.getDetailId())
                .tableType(resourceTag.getTableType())
                .tagDTOResponse(tag)
                .build();
    }

    public static ResourceTag toResourceTag(Tag tag, Resource resource) {
        return ResourceTag.builder()
                .active(true)
                .createdAt(LocalDateTime.now())
                .detailId(resource.getId())
                .tableType(TableType.resource_tbl)
                .resource(resource)
                .tag(tag)
                .build();
    }
}
