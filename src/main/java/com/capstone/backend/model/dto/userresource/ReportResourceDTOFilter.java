package com.capstone.backend.model.dto.userresource;

import com.capstone.backend.entity.type.ApproveType;
import com.capstone.backend.entity.type.TabResourceType;
import com.capstone.backend.entity.type.VisualType;
import com.capstone.backend.utils.Constants;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportResourceDTOFilter {
    TabResourceType tabResourceType;
    String resourceName;
    VisualType visualType;
    ApproveType approveType;
    Long pageIndex;
    Long pageSize;
}
