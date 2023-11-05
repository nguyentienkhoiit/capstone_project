package com.capstone.backend.model.dto.classes;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassDTORequest {
    @Schema(description = "Class is not null", example = "1A")
    @NotEmpty(message = "Class name is not null")
    @Length(min = 2, message = "Class name is greater than 1")
    String name;
}
