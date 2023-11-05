package com.capstone.backend.model.dto.role;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleDTORequest {
    String roleName;
    String description;
    List<Long> permission;
}
