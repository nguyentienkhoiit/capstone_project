package com.capstone.backend.model.dto.userresource;

import com.capstone.backend.entity.type.ActionType;
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
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResourceDTOResponse {
    Long resourceId;
    String resourceName;
    String username;
    LocalDateTime createdAt;
    ApproveType approveType;
    VisualType visualType;
    ResourceType resourceType;
    Boolean isUpdate = false;
    Boolean isShare = false;
    Boolean isDelete = false;
}
