package com.capstone.backend.model.dto.systempermission;

import com.capstone.backend.entity.type.MethodType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SystemPermissionDTOUpdate {
    Long permissionId;
    String permissionName;
    String path;
    MethodType methodType;
    String description;
    Long dependencyPermissionId;
}
