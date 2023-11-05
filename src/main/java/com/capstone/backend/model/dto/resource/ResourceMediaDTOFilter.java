package com.capstone.backend.model.dto.resource;

import com.capstone.backend.entity.type.ResourceType;
import com.capstone.backend.entity.type.TabResourceType;
import com.capstone.backend.utils.Constants;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResourceMediaDTOFilter {
    List<Long> listTags;
    String name;
    TabResourceType tabResourceType = TabResourceType.IMAGE;
    Long pageIndex = Constants.DEFAULT_PAGE_INDEX;
    Long pageSize = Constants.DEFAULT_PAGE_SIZE;
}
