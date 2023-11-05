package com.capstone.backend.model.dto.materials;

import com.capstone.backend.entity.type.TabResourceType;
import com.capstone.backend.utils.Constants;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MaterialsFilterDTORequest {
    Long classId;
    Long bookSeriesId;
    Long subjectId;
    Long bookVolumeId;
    Long chapterId;
    Long lessonId;
    TabResourceType tabResourceType;
    Long pageIndex = Constants.DEFAULT_PAGE_INDEX;
    Long pageSize = Constants.DEFAULT_PAGE_SIZE;
}
