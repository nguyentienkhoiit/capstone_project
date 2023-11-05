package com.capstone.backend.model.dto.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResourceDTORequest {

    @Schema(example = "1", description = "Subject must be integer")
    @NotNull(message = "{subjectId.not-null}")
    Long subjectId;

    @Schema(example = "1", description = "LessonId is optional")
    Long lessonId;

    @Schema(example = "Slide is beautiful", description = "Resource name is optional")
    String name;

    @Schema(example = "description", description = "Description is optional")
    String description;

    @Schema(description = "Visual type must be choose [PUBLIC, PRIVATE, RESTRICT]", example = "PUBLIC")
    @NotBlank(message = "{visualType.not-blank}")
    @Pattern(regexp = "(PUBLIC|PRIVATE|RESTRICT)", message = "{visualType.regex-message}")
    String visualType;

    @Schema(example = "[1, 2]", description = "Tag List must be Integer array")
    @NotEmpty(message = "{tagList.not-empty}")
    String tagList;
}
