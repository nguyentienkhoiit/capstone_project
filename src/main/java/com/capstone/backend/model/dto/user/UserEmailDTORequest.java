package com.capstone.backend.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEmailDTORequest {
    @Schema(example = "khoi@gmail.com")
    @NotBlank(message = "{email.not-blank}")
    @Pattern(regexp = "^(?:\\w+|\\w+([+\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.[a-zA-z]{2,4})+)$", message = "{email.regex-message}")
    String email;
}
