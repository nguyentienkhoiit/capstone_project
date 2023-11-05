package com.capstone.backend.model.dto.tag;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TagSuggestDTOResponse {
    Long tagId;
    String tagName;
}
