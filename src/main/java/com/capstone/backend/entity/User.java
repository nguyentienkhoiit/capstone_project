package com.capstone.backend.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "user_tbl")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String firstname;
    String lastname;
    @Column(unique = true)
    String username;
    @Column(unique = true)
    String email;
    String password;
    Boolean active;
    @Column(columnDefinition = "TEXT")
    String avatar;
    Boolean gender;
    LocalDate dateOfBirth;
    @Column(unique = true)
    String phone;
    String district;
    String village;
    String province;
    String school;
    @Column(updatable = false)
    LocalDateTime createdAt;
    Long violationTime;
    Long totalPoint;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = true)
    Class classObject;
    @OneToMany(mappedBy = "author")
    List<Resource> resourceOwnerList;

    @OneToMany(mappedBy = "moderator")
    List<Resource> resourceModList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch =  FetchType.EAGER)
    Set<UserRole> userRoleList = new HashSet<>();

    @OneToMany(mappedBy = "reporter", cascade = CascadeType.ALL)
    List<ReportResource> reportResourceList;

    @OneToMany(mappedBy = "commenter", cascade = CascadeType.ALL)
    List<Comment> commentList;

    @OneToMany(mappedBy = "reporter", cascade = CascadeType.ALL)
    List<ReportComment> reportCommentList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<UserResource> userResourceList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<UserResourcePermission> userResourcePermissionList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<ConfirmationToken> confirmationTokenList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        userRoleList.forEach(roleUser -> authorities.add(new SimpleGrantedAuthority(roleUser.getRole().getName())));
        return List.of(new SimpleGrantedAuthority(authorities.toString()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
