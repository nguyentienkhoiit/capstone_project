package com.capstone.backend.model.dto.systempermission;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionDTODisplay {
    Long permissionId;
    String permissionName;
}
