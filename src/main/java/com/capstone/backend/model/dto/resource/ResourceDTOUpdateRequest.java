package com.capstone.backend.model.dto.resource;

import com.capstone.backend.entity.type.VisualType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResourceDTOUpdateRequest {
    Long id;
    String name;
    String description;
    VisualType visualType;
    Long subjectId;
    Long lessonId;
    Set<Long> tagList;
}
