package com.capstone.backend.model.dto.register;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterDTORequest {

    @Schema(description = "Username has length greater than 3", example = "user")
    @NotBlank(message = "{username.not-blank}")
    @Length(min = 4, message = "{username.length}")
    String username;

    @Schema(example = "khoi@gmail.com")
    @Pattern(regexp = "^[a-zA-Z0-9.]+@(\\w+\\.)*(\\w+)$", message = "{email.regex-message}")
    String email;

    @Schema(description = "Password must contain at least 8 characters and include both letters and numbers",
            example = "1234567a")
    @NotBlank(message = "{password.not-blank}")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z0-9]{8,}$",
            message = "{password.regex-message}")
    String password;

}
