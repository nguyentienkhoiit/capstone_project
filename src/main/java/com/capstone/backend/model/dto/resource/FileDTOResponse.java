package com.capstone.backend.model.dto.resource;

import com.capstone.backend.entity.type.ResourceType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileDTOResponse {
    String resourceSrc;
    String thumbnailSrc;
    ResourceType resourceType;
    Long size;
    String fileExtension;
}
