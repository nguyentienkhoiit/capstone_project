package com.capstone.backend.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@Table(name = "comment_tbl")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long commentId;
    LocalDateTime createdAt;
    Boolean active;
    String content;

    @ManyToOne
    @JoinColumn(name = "resource_id")
    Resource resource;

    @ManyToOne
    @JoinColumn(name = "commenter_id")
    User commenter;

    @ManyToOne
    @JoinColumn(name = "comment_id_root", nullable = true)
    Comment commentRoot;

    @OneToMany(mappedBy = "commentRoot")
    List<Comment> commentRootList;

    @OneToMany(mappedBy = "comment")
    List<ReportComment> reportCommentList;
}
