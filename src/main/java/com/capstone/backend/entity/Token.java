package com.capstone.backend.entity;

import com.capstone.backend.entity.type.TokenType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@Table(name = "token_tbl")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String token;

    @Enumerated(EnumType.STRING)
    TokenType tokenType;

    Boolean expired;

    Boolean revoked;

    LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;


}
