package com.capstone.backend.model.dto.materials;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MaterialFilterProtectDTORequest {
    String classId;
    String bookSeriesId;
    String subjectId;
    String bookVolumeId;
    String chapterId;
    String lessonId;
    String tabResourceType;
    String pageIndex;
    String pageSize;
}
