package com.capstone.backend.service.impl;

import com.capstone.backend.entity.BookVolume;
import com.capstone.backend.entity.Chapter;
import com.capstone.backend.entity.User;
import com.capstone.backend.exception.ApiException;
import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.chapter.ChapterDTOFilter;
import com.capstone.backend.model.dto.chapter.ChapterDTORequest;
import com.capstone.backend.model.dto.chapter.ChapterDTOResponse;
import com.capstone.backend.model.mapper.ChapterMapper;
import com.capstone.backend.repository.BookVolumeRepository;
import com.capstone.backend.repository.ChapterRepository;
import com.capstone.backend.repository.LessonRepository;
import com.capstone.backend.repository.criteria.BookVolumeCriteria;
import com.capstone.backend.repository.criteria.ChapterCriteria;
import com.capstone.backend.service.ChapterService;
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
public class ChapterServiceImpl implements ChapterService {
    ChapterRepository chapterRepository;
    BookVolumeRepository bookVolumeRepository;
    LessonRepository lessonRepository;
    ChapterCriteria chapterCriteria;
    UserHelper userHelper;

    @Override
    public ChapterDTOResponse createChapter(ChapterDTORequest request) {
        User userLogged = userHelper.getUserLogin();
        // find book volume id
        BookVolume bookVolume = bookVolumeRepository
                .findById(request.getBookVolumeId())
                .orElseThrow(() -> ApiException.notFoundException("BookVolume is not found"));
        //add chapter
        Chapter chapter = Chapter.builder()
                .active(true)
                .createdAt(LocalDateTime.now())
                .name(request.getName())
                .userId(userLogged.getId())
                .bookVolume(bookVolume)
                .build();
        chapter = chapterRepository.save(chapter);
        return ChapterMapper.toChapterDTOResponse(chapter);
    }

    @Override
    public ChapterDTOResponse updateChapter(Long id, ChapterDTORequest request) {
        //find book volume id
        BookVolume bookVolume = bookVolumeRepository
                .findById(request.getBookVolumeId())
                .orElseThrow(() -> ApiException.notFoundException("BookVolume is not found"));
        // find chapter id want to update
        Chapter chapter = chapterRepository
                .findByIdAndActiveTrue(id)
                .orElseThrow(() -> ApiException.notFoundException("Chapter is not found"));
        //update
        chapter.setName(request.getName());
        chapter.setBookVolume(bookVolume);

        chapter = chapterRepository.save(chapter);
        return ChapterMapper.toChapterDTOResponse(chapter);
    }

    @Override
    public void deleteChapter(Long id) {
        //find id want to delete
        Chapter chapter = chapterRepository
                .findByIdAndActiveTrue(id)
                .orElseThrow(() -> ApiException.notFoundException("Chapter is not found"));
        //delete
        // check can delete
        if(isCanDelete(chapter)) {
            chapter.setActive(false);
            chapterRepository.save(chapter);
        } else {
           // throw exception warning
           throw ApiException.conflictResourceException("Can not delete Chapter because Lesson already exists");
        }

    }

    @Override
    public PagingDTOResponse searchChapter(ChapterDTOFilter chapterDTOFilter) {
        return chapterCriteria.searchChapter(chapterDTOFilter);
    }


    @Override
    public ChapterDTOResponse viewChapterById(Long id) {
        Chapter chapter = chapterRepository
                .findByIdAndActiveTrue(id)
                .orElseThrow(() -> ApiException.notFoundException("Chapter is not found"));
        return ChapterMapper.toChapterDTOResponse(chapter);
    }

    // Check exist lesson in chapter
    boolean isCanDelete(Chapter chapter){

        return !lessonRepository.existsLessonByChapterAndActiveTrue(chapter);
    }
}
