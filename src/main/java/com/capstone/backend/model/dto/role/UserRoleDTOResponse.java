package com.capstone.backend.model.dto.role;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRoleDTOResponse {
    Long userId;
    String username;
    String email;
    String avatar;
    List<RoleDTODisplay> roleDTODisplays;
}
