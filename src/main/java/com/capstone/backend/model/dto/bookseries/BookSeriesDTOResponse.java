package com.capstone.backend.model.dto.bookseries;

import com.capstone.backend.model.dto.classes.ClassDTOResponse;
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
public class BookSeriesDTOResponse {
    Long id;
    String name;
    Boolean active;
    Long userId;
    LocalDateTime createdAt;
    @JsonIgnore
    ClassDTOResponse classDTOResponse;
}
