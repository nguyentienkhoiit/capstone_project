package com.capstone.backend.model.dto.resource;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResourceViewDTOResponse {
    Long id;
    String name;
    String thumbnailSrc;
    Boolean isSave = false;
    Long point;
}
