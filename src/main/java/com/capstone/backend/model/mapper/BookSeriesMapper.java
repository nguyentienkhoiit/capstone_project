package com.capstone.backend.model.mapper;

import com.capstone.backend.entity.BookSeries;
import com.capstone.backend.model.dto.bookseries.BookSeriesDTOResponse;

public class BookSeriesMapper {
    public static BookSeriesDTOResponse toBookseriesDTOResponse(BookSeries bookSeries) {
        return BookSeriesDTOResponse.builder()
                .name(bookSeries.getName())
                .id(bookSeries.getId())
                .active(bookSeries.getActive())
                .createdAt(bookSeries.getCreatedAt())
                .classDTOResponse(ClassMapper.toClassDTOResponse(bookSeries.getClassObject()))
                .userId(bookSeries.getUserId())
                .build();
    }
}
