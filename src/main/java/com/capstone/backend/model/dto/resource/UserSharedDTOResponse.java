package com.capstone.backend.model.dto.resource;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSharedDTOResponse {
    Long userShareId;
    String email;
    String username;
    String permission;
}
