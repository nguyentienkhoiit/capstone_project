package com.capstone.backend.model.dto.resource;

import com.capstone.backend.entity.type.ApproveType;
import com.capstone.backend.entity.type.ResourceType;
import com.capstone.backend.entity.type.VisualType;
import com.capstone.backend.model.dto.tag.TagSuggestDTOResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResourceDTOUpdateResponse {
    Long id;
    String name;
    String description;
    ApproveType approveType;
    VisualType visualType;
    Long classId;
    Long bookSeriesId;
    Long subjectId;
    Long bookVolumeId;
    Long chapterId;
    Long lessonId;
    ResourceType resourceType;
    String resourceSrc;
    List<TagSuggestDTOResponse> tagList;
}
