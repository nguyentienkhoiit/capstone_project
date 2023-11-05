package com.capstone.backend.entity;

import com.capstone.backend.entity.type.MethodType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@Table(name = "system_permission_tbl")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SystemPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String name;
    Boolean active;
    LocalDateTime createdAt;
    String path;
    @Enumerated(EnumType.STRING)
    MethodType methodType;
    String description;
    Long userId;

    @ManyToOne
    @JoinColumn(name = "system_permission_id_root", nullable = true)
    SystemPermission systemPermissionRoot;

    @OneToMany(mappedBy = "systemPermissionRoot")
    List<SystemPermission> systemPermissionList;

    @OneToMany(mappedBy = "permission")
    List<UserRolePermission> userRolePermissionList;
}
