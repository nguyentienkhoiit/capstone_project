package com.capstone.backend.repository;

import com.capstone.backend.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    @Query("select r from Resource r where r.resourceSrc like %?1% and r.active = true")
    public Optional<Resource> findByResourceSrc(String fileName);

    @Query("select r from Resource r where r.resourceSrc = :filename or r.thumbnailSrc = :filename")
    public Optional<Resource> findResourceByResourceSrcOrThumbnailSrc(String filename);

    public Optional<Resource> findByIdAndActiveTrue(Long id);

    public Boolean existsResourceByLessonId(Long lessonId);

    @Query("select r.id from Resource r join r.lesson le join le.chapter cha " +
            "join cha.bookVolume bv join bv.bookSeriesSubject bss " +
            "join bss.bookSeries bs join bss.subject s join bs.classObject c where c.id in (:listClassIds)")
    public Set<Long> findResourceIdClass(Set<Long> listClassIds);

    @Query("select r.id from Resource r join r.lesson le join le.chapter cha " +
            "join cha.bookVolume bv join bv.bookSeriesSubject bss " +
            "join bss.bookSeries bs join bss.subject s where bs.id in (:listBookSeriesIds)")
    public Set<Long> findResourceIdBookSeries(Set<Long> listBookSeriesIds);

    @Query("select r.id from Resource r join r.lesson le join le.chapter cha " +
            "join cha.bookVolume bv join bv.bookSeriesSubject bss " +
            "join bss.subject s where s.id in (:listSubjectIds)")
    public Set<Long> findResourceIdSubject(Set<Long> listSubjectIds);

    @Query("select r.id from Resource r join r.lesson le join le.chapter cha " +
            "join cha.bookVolume bv where bv.id in (:listBookVolumeIds)")
    public Set<Long> findResourceIdBookVolume(Set<Long> listBookVolumeIds);

    @Query("select r.id from Resource r join r.lesson le join le.chapter cha where cha.id in (:listChapterIds)")
    public Set<Long> findResourceIdChapter(Set<Long> listChapterIds);

    @Query("select r.id from Resource r join r.lesson le where le.id in (:listLessonIds)")
    public Set<Long> findResourceIdLesson(Set<Long> listLessonIds);
}
