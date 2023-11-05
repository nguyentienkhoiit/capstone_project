package com.capstone.backend.repository;

import com.capstone.backend.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    @Query("select r from Resource r where r.resourceSrc like %?1% and r.active = true")
    public Optional<Resource> findByResourceSrc(String fileName);

    @Query("select r from Resource r where r.resourceSrc = :filename or r.thumbnailSrc = :filename")
    public Optional<Resource> findResourceByResourceSrcOrThumbnailSrc(String filename);

    public Optional<Resource> findByIdAndActiveTrue(Long id);

    public Boolean existsResourceByLessonId(Long lessonId);
}
