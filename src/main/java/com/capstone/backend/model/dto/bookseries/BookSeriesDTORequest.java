package com.capstone.backend.model.dto.bookseries;

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
public class BookSeriesDTORequest {

    @Schema(description = "Book series is not null", example = "chan troi sang tao")
    @NotEmpty(message = "Book series name is not null")
    @Length(min = 4, message = "Book series name is greater than 3")
    String name;

    @Schema(description = "Class id is mandatory", example = "1")
    @NotNull (message = "Class id is mandatory")
    Long classId;

}
