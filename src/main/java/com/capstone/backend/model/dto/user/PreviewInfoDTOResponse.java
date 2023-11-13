package com.capstone.backend.model.dto.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PreviewInfoDTOResponse {
    Long userId;
    String fullName;
    String avatar;
    Long savedCount;
    Long likeCount;
    Long uploadCount;
    String schoolName;
    String username;
}
