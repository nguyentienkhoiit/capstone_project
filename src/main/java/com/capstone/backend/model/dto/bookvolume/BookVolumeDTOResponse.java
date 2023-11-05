package com.capstone.backend.model.dto.bookvolume;

import com.capstone.backend.model.dto.classes.ClassDTOResponse;
import com.capstone.backend.model.dto.subject.SubjectDTOResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookVolumeDTOResponse {
    Long id;
    String name;
    Boolean active;
    Long userId;
    LocalDateTime createdAt;
    @JsonIgnore
    SubjectDTOResponse subjectDTOResponse;
}
