package com.capstone.backend.controller;

import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.bookseries.BookSeriesDTOFilter;
import com.capstone.backend.model.dto.bookseries.BookSeriesDTORequest;
import com.capstone.backend.model.dto.bookseries.BookSeriesDTOResponse;
import com.capstone.backend.service.BookSeriesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import static com.capstone.backend.utils.Constants.API_VERSION;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + "/book-series")
@Tag(name = "BookSeries", description = "API for Book Series")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CrossOrigin
public class BookSeriesController {
    BookSeriesService bookSeriesService;

    @Operation(summary = "Create book series")
    @PostMapping("")
    public ResponseEntity<BookSeriesDTOResponse> create(@Valid @RequestBody BookSeriesDTORequest request) {
        BookSeriesDTOResponse bookSeriesDTOResponse = bookSeriesService.createBookSeries(request);
        return ResponseEntity.ok(bookSeriesDTOResponse);
    }

    @Operation(summary = "Update book series")
    @PutMapping("/{id}")
    public ResponseEntity<BookSeriesDTOResponse> update(@Valid @RequestBody BookSeriesDTORequest request,
                                                        @PathVariable @NotEmpty Long id) {
        BookSeriesDTOResponse bookSeriesDTOResponse = bookSeriesService.updateBookSeries(id, request);
        return ResponseEntity.ok(bookSeriesDTOResponse);
    }

    @Operation(summary = "Delete book series")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable @NotEmpty Long id) {
        bookSeriesService.deleteBookSeries(id);
        return ResponseEntity.ok(true);
    }

    @Operation(summary = "Search Book series")
    @GetMapping("/display")
    public PagingDTOResponse searchBookSeries(@ModelAttribute BookSeriesDTOFilter bookSeriesDTOFilter) {
        return bookSeriesService.searchBookSeries(bookSeriesDTOFilter);
    }

    @Operation(summary = "View book series by Id")
    @GetMapping("/{id}")
    public ResponseEntity<BookSeriesDTOResponse> viewBookSeries(@PathVariable @NotEmpty Long id) {
        BookSeriesDTOResponse bookSeriesDTOResponse = bookSeriesService.viewBookSeriesById(id);
        return ResponseEntity.ok(bookSeriesDTOResponse);
    }
}
