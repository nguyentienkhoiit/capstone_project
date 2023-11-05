package com.capstone.backend.model.dto.materials;

import com.capstone.backend.entity.BookVolume;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubjectMaterialsDTOResponse {
    Long id;
    String name;
    boolean active = false;
    List<BookVolumeMaterialsDTOResponse> bookVolumes;
}
