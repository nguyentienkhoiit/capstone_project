package com.capstone.backend.service.impl;

import com.capstone.backend.entity.Chapter;
import com.capstone.backend.entity.Class;
import com.capstone.backend.entity.Lesson;
import com.capstone.backend.entity.User;
import com.capstone.backend.exception.ApiException;
import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.classes.ClassDTOResponse;
import com.capstone.backend.model.dto.lesson.LessonDTOFilter;
import com.capstone.backend.model.dto.lesson.LessonDTORequest;
import com.capstone.backend.model.dto.lesson.LessonDTOResponse;
import com.capstone.backend.model.mapper.ChapterMapper;
import com.capstone.backend.model.mapper.ClassMapper;
import com.capstone.backend.model.mapper.LessonMapper;
import com.capstone.backend.repository.ChapterRepository;
import com.capstone.backend.repository.LessonRepository;
import com.capstone.backend.repository.criteria.ChapterCriteria;
import com.capstone.backend.repository.criteria.LessonCriteria;
import com.capstone.backend.service.LessonService;
import com.capstone.backend.utils.UserHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LessonServiceImpl implements LessonService {
    LessonRepository lessonRepository;
    ChapterRepository chapterRepository;
    LessonCriteria lessonCriteria;
    UserHelper userHelper;
    @Override
    public LessonDTOResponse createLesson(LessonDTORequest request) {
        User userLogged = userHelper.getUserLogin();
        //find chapter id
        Chapter chapter = chapterRepository
                .findById(request.getChapterId())
                .orElseThrow(() -> ApiException.notFoundException("Chapter is not found"));
        // add lesson
        Lesson lesson = Lesson.builder()
                .active(true)
                .createdAt(LocalDateTime.now())
                .name(request.getName())
                .userId(userLogged.getId())
                .chapter(chapter)
                .build();
        lesson = lessonRepository.save(lesson);
        return LessonMapper.toLessonDTOResponse(lesson);
    }

    @Override
    public LessonDTOResponse updateLesson(Long id, LessonDTORequest request) {
        //find chapter id
        Chapter chapter = chapterRepository
                .findByIdAndActiveTrue(request.getChapterId())
                .orElseThrow(() -> ApiException.notFoundException("Chapter is not found"));
        //find Lesson id want to update
        Lesson lesson = lessonRepository
                .findByIdAndActiveTrue(id)
                .orElseThrow(() -> ApiException.notFoundException("Lesson is not found"));
        //update
        lesson.setName(request.getName());
        lesson.setChapter(chapter);

        lesson = lessonRepository.save(lesson);
        return LessonMapper.toLessonDTOResponse(lesson);
    }

    @Override
    public void deleteLesson(Long id) {
        //find Lesson id want to delete
        Lesson lesson = lessonRepository
                .findByIdAndActiveTrue(id)
                .orElseThrow(() -> ApiException.notFoundException("Lesson is not found"));
        //delete
        lesson.setActive(false);
        lessonRepository.save(lesson);
    }

    @Override
    public PagingDTOResponse searchLesson(LessonDTOFilter lessonDTOFilter) {
        return lessonCriteria.searchLesson(lessonDTOFilter);
    }


    @Override
    public LessonDTOResponse viewLessonById(Long id) {
        //find lesson id
        Lesson lesson = lessonRepository
                .findByIdAndActiveTrue(id)
                .orElseThrow(() -> ApiException.notFoundException("Lesson is not found"));
        return LessonMapper.toLessonDTOResponse(lesson);
    }


}
