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
public class PermissionDTOResponse {
    Long permissionId;
    String permissionName;
}
