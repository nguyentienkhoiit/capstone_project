package com.capstone.backend.model.dto.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTOCreate {
    String username;
    String email;
    String password;
    String firstname;
    String lastname;
    String phone;
    Boolean gender;
    Long districtId;
    Long provinceId;
    Boolean active;
    String avatar;
    Long dateOfBirth;
    String schoolName;
    Long createdAt;
    Long violationTime;
    Long totalPoint;
}
