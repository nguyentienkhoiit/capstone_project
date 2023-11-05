package com.capstone.backend.model.dto.role;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PagingRoleDTOResponse {
    Long totalPage;
    Long totalElement;
    Object data;
}
