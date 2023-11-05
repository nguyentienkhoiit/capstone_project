package com.capstone.backend.service.impl;

import com.capstone.backend.entity.BookSeries;
import com.capstone.backend.entity.Class;
import com.capstone.backend.entity.Subject;
import com.capstone.backend.entity.User;
import com.capstone.backend.exception.ApiException;
import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.bookseries.BookSeriesDTOFilter;
import com.capstone.backend.model.dto.bookseries.BookSeriesDTORequest;
import com.capstone.backend.model.dto.bookseries.BookSeriesDTOResponse;
import com.capstone.backend.model.dto.classes.ClassDTOFilter;
import com.capstone.backend.model.mapper.BookSeriesMapper;
import com.capstone.backend.repository.BookSeriesRepository;
import com.capstone.backend.repository.ClassRepository;
import com.capstone.backend.repository.SubjectRepository;
import com.capstone.backend.repository.criteria.BookSeriesCriteria;
import com.capstone.backend.repository.criteria.ClassesCriteria;
import com.capstone.backend.service.BookSeriesService;
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
public class BookSeriesServiceImpl implements BookSeriesService {
    BookSeriesRepository bookSeriesRepository;
    ClassRepository classRepository;
    SubjectRepository subjectRepository;
    BookSeriesCriteria bookSeriesCriteria;
    UserHelper userHelper;

    @Override
    public BookSeriesDTOResponse createBookSeries(BookSeriesDTORequest request) {
        User userLogged = userHelper.getUserLogin();
        // find class id
        Class classObject = classRepository
                .findById(request.getClassId())
                .orElseThrow(() -> ApiException.notFoundException("Class is not found"));

        BookSeries bookSeries = BookSeries.builder()
                .active(true)
                .createdAt(LocalDateTime.now())
                .name(request.getName())
                .userId(userLogged.getId())
                .classObject(classObject)
                .build();

        bookSeries = bookSeriesRepository.save(bookSeries);
        return BookSeriesMapper.toBookseriesDTOResponse(bookSeries);
    }

    @Override
    public BookSeriesDTOResponse updateBookSeries(Long id, BookSeriesDTORequest request) {
        // find BookSeries id
        BookSeries bookSeries = bookSeriesRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("BookSeries is not found"));
        // find class id
        Class classObject = classRepository
                .findByIdAndActiveTrue(request.getClassId())
                .orElseThrow(() -> ApiException.notFoundException("Class is not found"));
        // update book series
        bookSeries.setName(request.getName());
        bookSeries.setClassObject(classObject);


        bookSeries = bookSeriesRepository.save(bookSeries);
        return BookSeriesMapper.toBookseriesDTOResponse(bookSeries);
    }

    @Override
    public void deleteBookSeries(Long id) {
        // find BookSeries id
        BookSeries bookSeries = bookSeriesRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("BookSeries is not found"));
        if (isCanDelete(bookSeries)) {
            bookSeries.setActive(false);
            bookSeriesRepository.save(bookSeries);
        } else {
            // throw exception warning
            throw ApiException.conflictResourceException("Can not delete BookSeries because Subject already exists");
        }
    }

    @Override
    public PagingDTOResponse searchBookSeries(BookSeriesDTOFilter bookSeriesDTOFilter) {
        return bookSeriesCriteria.searchBookSeries(bookSeriesDTOFilter);
    }


    @Override
    public BookSeriesDTOResponse viewBookSeriesById(Long id) {
        BookSeries bookSeries = bookSeriesRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("BookSeries is not found"));
        return BookSeriesMapper.toBookseriesDTOResponse(bookSeries);
    }
    // Check exist subject in book series
    boolean isCanDelete(BookSeries bookSeries) {
        return true;
    }
}
