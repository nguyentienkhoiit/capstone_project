package com.capstone.backend.model.dto.systempermission;

import com.capstone.backend.entity.type.MethodType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SystemPermissionDTOResponse {
    Long systemPermissionId;
    String permissionName;
    String path;
    MethodType methodType;
    Boolean active;
    LocalDateTime createdAt;
    String description;
    String creator;
    Long dependencyPermissionId;
}
