package com.capstone.backend.model.dto.materials;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonMaterialsDTOResponse {
    Long id;
    String name;
    boolean active = false;
    PagingMaterialDTOResponse pagingMaterialDTOResponse;
}
