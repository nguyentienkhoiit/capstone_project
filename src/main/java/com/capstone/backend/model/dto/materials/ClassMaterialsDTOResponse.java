package com.capstone.backend.model.dto.materials;

import com.capstone.backend.entity.BookSeries;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassMaterialsDTOResponse {
    Long id;
    String name;
    boolean active = false;
    List<BookSeriesMaterialsDTOResponse> bookSeries;
}
