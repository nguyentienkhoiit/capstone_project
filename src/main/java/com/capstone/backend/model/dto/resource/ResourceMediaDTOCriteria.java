package com.capstone.backend.model.dto.resource;

import com.capstone.backend.entity.type.TableType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResourceMediaDTOCriteria {
    Long detailId;
    TableType tableType;
}
