package com.capstone.backend.repository;

import com.capstone.backend.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByIdAndActiveTrue(Long id);

    Optional<Tag> findByNameEqualsIgnoreCaseAndActiveTrue(String name);

    @Query("select DISTINCT rt.tag from ResourceTag rt where rt.active = true and rt.tag.active = true and rt.tag.name like %:tagName%")
    List<Tag> findAllByNameContainsAndActive(String tagName);

    @Query("select t from Tag t where t.active = true and t.name like %:tagName%")
    List<Tag> findGlobalTagByNameContainsAndActive(String tagName);
}
