package com.capstone.backend.model.dto.chapter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChapterDTORequest {
    @Schema(description = "Chapter is not null", example = " Cac phep toan co ban")
    @NotEmpty(message = "Chapter name is not null")
    @Length(min = 4, message = "Chapter name is greater than 3")
    String name;

    @Schema(description = "Chapter id is mandatory", example = "1")
    @NotNull(message = "Chapter id is mandatory")
    Long bookVolumeId;
}
