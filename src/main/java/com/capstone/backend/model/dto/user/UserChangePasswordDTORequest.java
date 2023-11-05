package com.capstone.backend.model.dto.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserChangePasswordDTORequest {
    String currentPassword;
    String newPassword;
    String confirmationPassword;
}
