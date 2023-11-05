package com.capstone.backend.service.impl;

import com.capstone.backend.entity.*;
import com.capstone.backend.exception.ApiException;
import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.lesson.LessonDTOResponse;
import com.capstone.backend.model.dto.subject.SubjectDTOFilter;
import com.capstone.backend.model.dto.subject.SubjectDTORequest;
import com.capstone.backend.model.dto.subject.SubjectDTOResponse;
import com.capstone.backend.model.mapper.LessonMapper;
import com.capstone.backend.model.mapper.SubjectMapper;
import com.capstone.backend.repository.BookSeriesRepository;
import com.capstone.backend.repository.BookVolumeRepository;
import com.capstone.backend.repository.SubjectRepository;
import com.capstone.backend.repository.criteria.BookSeriesCriteria;
import com.capstone.backend.repository.criteria.SubjectCriteria;
import com.capstone.backend.service.SubjectService;
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
public class SubjectServiceImpl implements SubjectService {
    SubjectRepository subjectRepository;
    BookSeriesRepository bookSeriesRepository;
    BookVolumeRepository bookVolumeRepository;
    SubjectCriteria subjectCriteria;

    @Override
    public SubjectDTOResponse createSubject(SubjectDTORequest request) {
        // current user is login by authentication.getPrincipal()
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userLogged = (User) authentication.getPrincipal();

        // find book series id
        BookSeries bookSeriesObject = bookSeriesRepository
                .findById(request.getBookseriesId())
                .orElseThrow(() -> ApiException.notFoundException("Book series is not found"));

        // add entity
        Subject subject = Subject.builder()
                .active(true)
                .createdAt(LocalDateTime.now())
                .name(request.getName())
                .userId(userLogged.getId())
//                .bookSeries(bookSeriesObject)
                .build();

        subject = subjectRepository.save(subject);
        return SubjectMapper.toSubjectDTOResponse(subject);

    }

    @Override
    public SubjectDTOResponse updateSubject(Long id, SubjectDTORequest request) {

        // find book series id
        BookSeries bookSeriesObject = bookSeriesRepository
                .findById(request.getBookseriesId())
                .orElseThrow(() -> ApiException.notFoundException("Book series is not found"));
        // find subject id want update
        Subject subject = subjectRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("Subject is not found"));
        // update subject
         subject.setName(request.getName());
//         subject.setBookSeries(bookSeriesObject);

        subject = subjectRepository.save(subject);
        return SubjectMapper.toSubjectDTOResponse(subject);

    }

    @Override
    public void deleteSubject(Long id) {
        // find Subject id
        Subject subject = subjectRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("Subject is not found"));
        if (isCanDelete(subject)) {
            subject.setActive(false);
            subjectRepository.save(subject);
        } else {
            // throw exception warning
            throw ApiException.conflictResourceException("Can not delete Subject because BookVolume already exists");
        }
    }

    @Override
    public PagingDTOResponse searchSubject(SubjectDTOFilter subjectDTOFilter) {
        return subjectCriteria.searchSubject(subjectDTOFilter);
    }


    @Override
    public SubjectDTOResponse viewSubjectById(Long id) {
        Subject subject = subjectRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("Subject is not found"));

        return SubjectMapper.toSubjectDTOResponse(subject);
    }

    // Check exist book volume in subject
    boolean isCanDelete(Subject subject) {
        return true;
    }
}
