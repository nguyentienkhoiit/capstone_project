package com.capstone.backend.model.dto.resource;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassLessonId {
    Long classId;
    Long bookSeriesId;
    Long subjectId;
    Long bookVolumeId;
    Long chapterId;
    Long lessonId;
}
