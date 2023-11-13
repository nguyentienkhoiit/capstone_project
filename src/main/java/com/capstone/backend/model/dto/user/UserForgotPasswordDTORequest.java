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
public class UserForgotPasswordDTORequest {
    String token;
    @Schema(description = "Password must contain at least 8 characters and include both letters and numbers", example = "1234567a")
    @NotBlank(message = "{password.not-blank}")
    @Pattern(regexp = "^(?=.*[A-Za-z@])(?=.*[0-9])[A-Za-z0-9]{8,}$", message = "{password.regex-message}")
    String newPassword;
    @Schema(description = "Password must contain at least 8 characters and include both letters and numbers", example = "1234567a")
    @NotBlank(message = "{password.not-blank}")
    @Pattern(regexp = "^(?=.*[A-Za-z@])(?=.*[0-9])[A-Za-z0-9]{8,}$", message = "{password.regex-message}")
    String confirmationPassword;
}
