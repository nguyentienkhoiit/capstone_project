package com.capstone.backend.model.dto.tag;


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

public class TagDTORequest {
    @Length(max = 25, message = "Cannot be greater than 25")
    @NotEmpty(message = "Not empty")
    String name;
}
