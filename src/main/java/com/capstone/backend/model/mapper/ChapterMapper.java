package com.capstone.backend.model.mapper;

import com.capstone.backend.entity.Chapter;
import com.capstone.backend.model.dto.chapter.ChapterDTOResponse;

public class ChapterMapper {
    public static ChapterDTOResponse toChapterDTOResponse(Chapter chapter) {
        return ChapterDTOResponse.builder()
                .name(chapter.getName())
                .id(chapter.getId())
                .active(chapter.getActive())
                .createdAt(chapter.getCreatedAt())
                .bookVolumeDTOResponse(BookVolumeMapper.toBookVolumeDTOResponse(chapter.getBookVolume()))
                .userId(chapter.getUserId())
                .build();
    }
}
