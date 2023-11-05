package com.capstone.backend.model.dto.role;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleDTODisplay {
    Long roleId;
    String roleName;
}
