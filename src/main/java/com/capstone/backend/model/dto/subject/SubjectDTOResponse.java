package com.capstone.backend.model.dto.subject;

import com.capstone.backend.model.dto.bookseries.BookSeriesDTOResponse;
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
public class SubjectDTOResponse {
    Long id;
    String name;
    boolean active;
    Long userId;
    LocalDateTime createdAt;
    @JsonIgnore
    BookSeriesDTOResponse bookSeriesDTOResponse;
}
