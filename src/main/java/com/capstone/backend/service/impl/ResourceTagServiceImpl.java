package com.capstone.backend.service.impl;

import com.capstone.backend.entity.ResourceTag;
import com.capstone.backend.entity.Tag;
import com.capstone.backend.entity.type.TableType;
import com.capstone.backend.exception.ApiException;
import com.capstone.backend.model.dto.resourcetag.ResourceTagDTOResponse;
import com.capstone.backend.model.dto.tag.TagDTOResponse;
import com.capstone.backend.model.mapper.ResourceTagMapper;
import com.capstone.backend.repository.ResourceTagRepository;
import com.capstone.backend.service.ResourceTagService;
import com.capstone.backend.utils.MessageException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ResourceTagServiceImpl implements ResourceTagService {
    ResourceTagRepository resourceTagRepository;
    MessageException messageException;

    public List<ResourceTagDTOResponse> getAllResourceTagByTableTypeAndID(String tableType, long detailId) {
        List<ResourceTag> resourceTagList = resourceTagRepository.getAllResourceTagByTableTypeAndID(TableType.valueOf(tableType), detailId);
        return resourceTagList.stream()
                .map(ResourceTagMapper::toResourceTagDTOResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ResourceTagDTOResponse applyTagToResource(TagDTOResponse tagDTOResponse, String tableType, long detailId) {
        // Check if this tag name has been applied before
        if (resourceTagRepository.findByTagIdAndByTableNameAndRowID(TableType.valueOf(tableType), detailId, tagDTOResponse.getId()).isPresent()) {
            throw ApiException.badRequestException(messageException.MSG_TAG_APPLIED);
        }
        ResourceTag resourceTag = ResourceTag.builder()
                .detailId(detailId)
                .tableType(TableType.valueOf(tableType))
                .createdAt(LocalDateTime.now())
                .active(true)
                .tag(Tag.builder()
                        .id(tagDTOResponse.getId())
                        .build())
                .build();
        resourceTagRepository.save(resourceTag);
        return ResourceTagMapper.toResourceTagDTOResponse(resourceTag);
    }

    @Override
    public ResourceTagDTOResponse disableTagFromResource(long id) {
        ResourceTag resourceTag = resourceTagRepository.findById(id)
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_RESOURCE_NOT_FOUND));
        resourceTag.setActive(false);
        resourceTagRepository.save(resourceTag);
        return ResourceTagMapper.toResourceTagDTOResponse(resourceTag);
    }
}
