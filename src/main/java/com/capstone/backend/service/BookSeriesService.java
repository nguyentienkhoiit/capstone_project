package com.capstone.backend.service;

import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.bookseries.BookSeriesDTOFilter;
import com.capstone.backend.model.dto.bookseries.BookSeriesDTORequest;
import com.capstone.backend.model.dto.bookseries.BookSeriesDTOResponse;
import com.capstone.backend.model.dto.classes.ClassDTOFilter;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookSeriesService {
    public BookSeriesDTOResponse createBookSeries(BookSeriesDTORequest request);

    public BookSeriesDTOResponse updateBookSeries(Long id, BookSeriesDTORequest request);

    public void deleteBookSeries(Long id);

    public PagingDTOResponse searchBookSeries(BookSeriesDTOFilter bookSeriesDTOFilter);

    public BookSeriesDTOResponse viewBookSeriesById(Long id);
}
