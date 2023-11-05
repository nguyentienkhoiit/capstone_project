package com.capstone.backend.model.dto.role;

import com.capstone.backend.utils.Constants;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleDTOFilter {
    String name;
    Boolean active;
    Long pageSize = Constants.DEFAULT_PAGE_SIZE;
    Long pageIndex = Constants.DEFAULT_PAGE_INDEX;
}
