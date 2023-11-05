package com.capstone.backend.model.dto.userresource;

import com.capstone.backend.entity.type.TabResourceType;
import com.capstone.backend.utils.Constants;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResourceSavedOrSharedDTOFilter {
    TabResourceType tabResourceType;
    String resourceName;
    Long pageIndex = Constants.DEFAULT_PAGE_INDEX;
    Long pageSize = Constants.DEFAULT_PAGE_SIZE;
}
