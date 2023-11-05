package com.capstone.backend.repository;

import com.capstone.backend.entity.ReportComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReportCommentRepository extends JpaRepository<ReportComment, Long> {

    @Query("select rc from ReportComment rc where rc.reporter.id = ?1 and rc.active = true")
    public Optional<ReportComment> findByReporterIdActive(Long reportId);
}
