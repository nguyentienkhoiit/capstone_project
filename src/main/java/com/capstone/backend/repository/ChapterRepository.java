package com.capstone.backend.repository;

import com.capstone.backend.entity.BookVolume;
import com.capstone.backend.entity.Chapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    Page<Chapter> findChapterByNameContainsAndActiveTrue(String name, Pageable pageable);
    Optional<Chapter> findByIdAndActiveTrue(Long id);
    boolean existsChapterByBookVolumeAndActiveTrue(BookVolume bookVolume);
}
