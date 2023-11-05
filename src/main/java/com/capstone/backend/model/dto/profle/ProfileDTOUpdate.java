package com.capstone.backend.model.dto.profle;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileDTOUpdate {
    String firstname;
    String lastname;
    Boolean gender;
    LocalDate dateOfBirth;
    String phone;
    String district;
    String province;
    String village;
    String school;
    Long classId;
}
