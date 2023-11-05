package com.capstone.backend.service.impl;

import com.capstone.backend.entity.*;
import com.capstone.backend.exception.ApiException;
import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.bookseries.BookSeriesDTOResponse;
import com.capstone.backend.model.dto.bookvolume.BookVolumeDTOFilter;
import com.capstone.backend.model.dto.bookvolume.BookVolumeDTORequest;
import com.capstone.backend.model.dto.bookvolume.BookVolumeDTOResponse;
import com.capstone.backend.model.mapper.BookSeriesMapper;
import com.capstone.backend.model.mapper.BookVolumeMapper;
import com.capstone.backend.model.mapper.SubjectMapper;
import com.capstone.backend.repository.BookVolumeRepository;
import com.capstone.backend.repository.ChapterRepository;
import com.capstone.backend.repository.SubjectRepository;
import com.capstone.backend.repository.criteria.BookVolumeCriteria;
import com.capstone.backend.repository.criteria.SubjectCriteria;
import com.capstone.backend.service.BookVolumeService;
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
public class BookVolumeServiceImpl implements BookVolumeService {
    BookVolumeRepository bookVolumeRepository;
    SubjectRepository subjectRepository;
    ChapterRepository chapterRepository;
    BookVolumeCriteria bookVolumeCriteria;
    UserHelper userHelper;

    @Override
    public BookVolumeDTOResponse createBookVolume(BookVolumeDTORequest request) {
        User userLogged = userHelper.getUserLogin();

        // find subject id
        Subject subject = subjectRepository
                .findById(request.getSubjectId())
                .orElseThrow(() -> ApiException.notFoundException("Subject is not found"));

        // add entity
        BookVolume bookVolume = BookVolume.builder()
                .active(true)
                .createdAt(LocalDateTime.now())
                .name(request.getName())
                .userId(userLogged.getId())
//                .subject(subject)
                .build();

        bookVolume = bookVolumeRepository.save(bookVolume);
        return BookVolumeMapper.toBookVolumeDTOResponse(bookVolume);
    }

    @Override
    public BookVolumeDTOResponse updateBookVolume(Long id, BookVolumeDTORequest request) {

        // find subject id
        Subject subject = subjectRepository
                .findById(request.getSubjectId())
                .orElseThrow(() -> ApiException.notFoundException("Subject is not found"));
        //find BookVolume want update
        BookVolume bookVolume = bookVolumeRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("BookVolume is not found"));
        //update BookVolume
        bookVolume.setName(request.getName());
//        bookVolume.setSubject(subject);

        bookVolume = bookVolumeRepository.save(bookVolume);
        return BookVolumeMapper.toBookVolumeDTOResponse(bookVolume);
    }

    @Override
    public void deleteBookVolume(Long id) {
        //find BookVolume want delete
        BookVolume bookVolume = bookVolumeRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("BookVolume is not found"));
        if (isCanDelete(bookVolume)) {
            bookVolume.setActive(false);
            bookVolumeRepository.save(bookVolume);
        } else {
            // throw exception warning
            throw ApiException.conflictResourceException("Can not delete BookVolume because Chapter already exists");
        }

    }

    @Override
    public PagingDTOResponse searchBookVolume(BookVolumeDTOFilter bookVolumeDTOFilter) {
        return bookVolumeCriteria.searchBookVolume(bookVolumeDTOFilter);
    }


    @Override
    public BookVolumeDTOResponse viewBookVolumeById(Long id) {
        BookVolume bookVolume = bookVolumeRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("BookVolume is not found"));
        return BookVolumeMapper.toBookVolumeDTOResponse(bookVolume);
    }

    // Check exist chapter in book volume
    boolean isCanDelete(BookVolume bookVolume) {
        return !chapterRepository.existsChapterByBookVolumeAndActiveTrue(bookVolume);
    }
}