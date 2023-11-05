package com.capstone.backend.model.dto.classes;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassDTOResponse {
    Long id;
    String name;
    boolean active;
    LocalDateTime createdAt;
    Long userId;
}
