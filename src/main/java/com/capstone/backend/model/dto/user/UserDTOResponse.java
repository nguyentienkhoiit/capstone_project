package com.capstone.backend.model.dto.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTOResponse {
    Long id;
    String firstname;
    String lastname;
    String username;
    String email;
    Boolean active;
    String avatar;
    Boolean gender;
    LocalDate dateOfBirth;
    String phone;
    String district;
    String province;
    String school;
    LocalDateTime createdAt;
}
