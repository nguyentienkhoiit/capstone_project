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
@Table(name = "chapter_tbl")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String name;
    Boolean active;
    LocalDateTime createdAt;
    @Column(nullable = false)
    Long userId;

    @ManyToOne
    @JoinColumn(name = "book_volume_id")
    BookVolume bookVolume;

    @OneToMany(mappedBy = "chapter")
    List<Lesson> lessonList;
}
