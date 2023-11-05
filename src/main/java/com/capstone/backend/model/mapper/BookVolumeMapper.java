package com.capstone.backend.model.mapper;

import com.capstone.backend.entity.BookVolume;
import com.capstone.backend.model.dto.bookvolume.BookVolumeDTOResponse;
import com.capstone.backend.model.dto.subject.SubjectDTOResponse;

public class BookVolumeMapper {
    public static BookVolumeDTOResponse toBookVolumeDTOResponse(BookVolume bookVolume) {
        return BookVolumeDTOResponse.builder()
                .name(bookVolume.getName())
                .id(bookVolume.getId())
                .active(bookVolume.getActive())
                .createdAt(bookVolume.getCreatedAt())
//                .subjectDTOResponse(SubjectMapper.toSubjectDTOResponse(bookVolume.getSubject()))
                .userId(bookVolume.getUserId())
                .build();
    }
}
