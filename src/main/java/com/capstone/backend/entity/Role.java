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
@Table(name = "role_tbl")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false, unique = true)
    String name;
    LocalDateTime createdAt;
    Boolean active;
    String description;
    Long userId;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    List<UserRole> userRoleList;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    List<UserRolePermission> userRolePermissionList;
}
