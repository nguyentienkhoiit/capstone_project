package com.capstone.backend.service;

import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.chapter.ChapterDTOFilter;
import com.capstone.backend.model.dto.chapter.ChapterDTOResponse;
import com.capstone.backend.model.dto.classes.ClassDTOResponse;
import com.capstone.backend.model.dto.lesson.LessonDTOFilter;
import com.capstone.backend.model.dto.lesson.LessonDTORequest;
import com.capstone.backend.model.dto.lesson.LessonDTOResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LessonService {
    public LessonDTOResponse createLesson(LessonDTORequest request);

    public LessonDTOResponse updateLesson(Long id, LessonDTORequest request);

    public void deleteLesson(Long id);

    public PagingDTOResponse searchLesson(LessonDTOFilter lessonDTOFilter);

    public LessonDTOResponse viewLessonById(Long id);
}
