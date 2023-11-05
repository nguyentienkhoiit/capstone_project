package com.capstone.backend.model.mapper;

import com.capstone.backend.entity.Tag;
import com.capstone.backend.model.dto.tag.TagDTORequest;
import com.capstone.backend.model.dto.tag.TagDTOResponse;
import com.capstone.backend.model.dto.tag.TagSuggestDTORequest;
import com.capstone.backend.model.dto.tag.TagSuggestDTOResponse;

import java.time.LocalDateTime;

public class TagMapper {
    public static Tag toTag(TagDTORequest tagDTORequest) {
        return Tag.builder()
                .name(tagDTORequest.getName())
                .createdAt(LocalDateTime.now())
                .active(true)
                .build();
    }

    public static TagDTOResponse toTagDTOResponse(Tag tag) {
        return TagDTOResponse.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
    }

    public static TagSuggestDTOResponse toTagSuggestDTOResponse(Tag tag) {
        return TagSuggestDTOResponse.builder()
                .tagId(tag.getId())
                .tagName(tag.getName())
                .build();
    }
}
