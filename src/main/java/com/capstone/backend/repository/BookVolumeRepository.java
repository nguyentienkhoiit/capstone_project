package com.capstone.backend.repository;

import com.capstone.backend.entity.BookSeries;
import com.capstone.backend.entity.BookVolume;
import com.capstone.backend.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookVolumeRepository extends JpaRepository<BookVolume, Long> {
//    Page<BookVolume> findBookVolumeByNameContainsAndActiveTrue(String name, Pageable pageable);
    Optional<BookVolume> findByIdAndActiveTrue(Long id);
//    boolean existsBookVolumeBySubjectAndActiveTrue(Subject subject);
}

