package com.capstone.backend.model.mapper;

import com.capstone.backend.entity.Lesson;
import com.capstone.backend.model.dto.lesson.LessonDTOResponse;

public class LessonMapper {
    public static LessonDTOResponse toLessonDTOResponse(Lesson lesson) {
        return LessonDTOResponse.builder()
                .name(lesson.getName())
                .id(lesson.getId())
                .active(lesson.getActive())
                .createdAt(lesson.getCreatedAt())
                .chapterDTOResponse(ChapterMapper.toChapterDTOResponse(lesson.getChapter()))
                .userId(lesson.getUserId())
                .build();
    }
}
