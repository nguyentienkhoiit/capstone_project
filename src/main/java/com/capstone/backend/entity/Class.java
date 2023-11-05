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
@Table(name = "class_tbl")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Class {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String name;
    Boolean active;
    LocalDateTime createdAt;
    @Column(nullable = false)
    Long userId;

    @OneToMany(mappedBy = "classObject")
    List<User> userList;

    @OneToMany(mappedBy = "classObject")
    List<BookSeries> bookSeriesList;
}
