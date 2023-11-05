package com.capstone.backend.model.dto.lesson;

import com.capstone.backend.model.dto.bookvolume.BookVolumeDTOResponse;
import com.capstone.backend.model.dto.chapter.ChapterDTOResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonDTOResponse {
    Long id;
    String name;
    Boolean active;
    Long userId;
    LocalDateTime createdAt;
    @JsonIgnore
    ChapterDTOResponse chapterDTOResponse;
}
