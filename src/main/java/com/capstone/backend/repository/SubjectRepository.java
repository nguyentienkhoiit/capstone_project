package com.capstone.backend.repository;

import com.capstone.backend.entity.BookSeries;
import com.capstone.backend.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    public Optional<Subject> findByIdAndActiveTrue(Long id);
}
