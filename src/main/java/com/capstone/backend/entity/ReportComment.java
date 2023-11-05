package com.capstone.backend.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "report_comment_tbl")
public class ReportComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false, columnDefinition = "TEXT")
    String message;
    Boolean active;
    LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "reporter_id")
    User reporter;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    Comment comment;
}
