package com.capstone.backend.model.dto.materials;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PagingMaterialDTOResponse {
    Long totalPage;
    Long totalElement;
    Object data;
}
