package com.capstone.backend.model.mapper;

import com.capstone.backend.entity.Subject;
import com.capstone.backend.model.dto.subject.SubjectDTOResponse;

public class SubjectMapper {
    public static SubjectDTOResponse toSubjectDTOResponse(Subject subject) {
        return SubjectDTOResponse.builder()
                .name(subject.getName())
                .id(subject.getId())
                .active(subject.getActive())
                .createdAt(subject.getCreatedAt())
//                .bookSeriesDTOResponse(BookSeriesMapper.toBookseriesDTOResponse(subject.getBookSeries()))
                .userId(subject.getUserId())
                .build();
    }
}
