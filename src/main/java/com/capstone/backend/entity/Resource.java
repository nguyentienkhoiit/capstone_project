package com.capstone.backend.entity;

import com.capstone.backend.entity.type.ApproveType;
import com.capstone.backend.entity.type.ResourceType;
import com.capstone.backend.entity.type.TabResourceType;
import com.capstone.backend.entity.type.VisualType;
import com.capstone.backend.utils.Constants;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@Table(name = "resource_tbl")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = true)
    String name;

    @Column(columnDefinition = "TEXT")
    String description;

    @Enumerated(EnumType.STRING)
    ResourceType resourceType;

    LocalDateTime createdAt;

    Boolean active;

    @Enumerated(EnumType.STRING)
    ApproveType approveType;

    @Enumerated(EnumType.STRING)
    VisualType visualType;

    @Enumerated(EnumType.STRING)
    TabResourceType tabResourceType;

    String thumbnailSrc;

    @Column(nullable = false)
    String resourceSrc;

    Long point = Constants.POINT_RESOURCE;

    Long size;


    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = true)
    Lesson lesson;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    Subject subject;

    @ManyToOne
    @JoinColumn(name = "author_id")
    User author;

    @ManyToOne
    @JoinColumn(name = "mod_id", nullable = true)
    User moderator;

    @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL)
    List<ReportResource> reportResourceList;

    @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL)
    List<Comment> commentList;

    @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL)
    List<UserResource> userResourceList;

    @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL)
    List<UserResourcePermission> userResourcePermissionList;

    @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL)
    List<ResourceTag> resourceTagList;
}
