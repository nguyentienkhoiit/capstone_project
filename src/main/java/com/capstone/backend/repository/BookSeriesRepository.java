package com.capstone.backend.repository;

import com.capstone.backend.entity.BookSeries;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookSeriesRepository extends JpaRepository<BookSeries, Long> {
//    Page<BookSeries> findBookSeriesByNameContainsAndActiveTrue(String name, Pageable pageable);
//
    Optional<BookSeries> findByIdAndActiveTrue(Long id);
}
