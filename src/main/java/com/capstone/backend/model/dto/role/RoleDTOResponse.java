package com.capstone.backend.model.dto.role;

import com.capstone.backend.model.dto.systempermission.PermissionDTOResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleDTOResponse {
    Long roleId;
    String roleName;
    String description;
    LocalDateTime createdAt;
    Boolean active;
    String creator;
}
