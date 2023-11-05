package com.capstone.backend.model.dto.resource;


import com.capstone.backend.entity.type.VisualType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResourceSharedDTORequest {
    Long resourceId;
    List<Long> userShareIds;
    VisualType visualType;
}
