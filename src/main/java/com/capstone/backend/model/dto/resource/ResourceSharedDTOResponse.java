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
public class ResourceSharedDTOResponse {
    Long resourceId;
    String resourceName;
    List<UserSharedDTOResponse> userSharedDTOResponses;
    VisualType visualType;
}
