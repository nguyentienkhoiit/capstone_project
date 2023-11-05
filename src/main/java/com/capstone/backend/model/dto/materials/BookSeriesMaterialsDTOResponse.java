package com.capstone.backend.model.dto.materials;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookSeriesMaterialsDTOResponse {
    Long id;
    String name;
    boolean active = false;
    List<SubjectMaterialsDTOResponse> subjects;
}
