package com.capstone.backend.model.dto.authentication;

import com.capstone.backend.model.dto.role.RoleDTODisplay;
import com.capstone.backend.model.dto.role.RoleDTOResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationDTOResponse {
    @JsonProperty("access_token")
    String accessToken;
//    List<RoleDTODisplay> roleDTOResponses;
}
