package com.capstone.backend.model.dto.profle;

import com.capstone.backend.model.dto.classes.ClassDTOResponse;
import com.capstone.backend.model.dto.role.RoleDTODisplay;
import com.capstone.backend.model.dto.role.RoleDTOResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileDTOResponse {
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
    String village;
    String school;
    LocalDateTime createdAt;
    Long classId;
    List<RoleDTODisplay> roleDTOResponses;
}
