package com.capstone.backend.repository;


import com.capstone.backend.entity.Class;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClassRepository extends JpaRepository<Class, Long> {
    Page<Class> findClassByNameContainsAndActiveTrue(String name, Pageable pageable);

    Optional<Class> findByIdAndActiveTrue(Long id);
    public List<Class> findClassByActiveIsTrue();
}
