package com.capstone.backend.entity;

import com.capstone.backend.entity.type.TableType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@Table(name = "resource_tag_tbl")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResourceTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long detailId;
    @Enumerated(EnumType.STRING)
    TableType tableType;
    LocalDateTime createdAt;
    Boolean active;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    Tag tag;

    @ManyToOne
    @JoinColumn(name = "resource_id")
    Resource resource;
}
