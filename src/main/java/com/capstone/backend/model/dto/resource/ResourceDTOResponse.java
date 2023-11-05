package com.capstone.backend.model.dto.resource;

import com.capstone.backend.entity.User;
import com.capstone.backend.entity.type.ApproveType;
import com.capstone.backend.entity.type.ResourceType;
import com.capstone.backend.entity.type.VisualType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResourceDTOResponse {
    Long id;
    String name;
    String description;
    ResourceType resourceType;
    LocalDateTime createdAt;
    Boolean active;
    ApproveType approveType;
    VisualType visualType;
    String thumbnailSrc;
    String resourceSrc;
    Long point;
    Long size;
}
