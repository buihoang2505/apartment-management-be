package com.apartment.domain.user;

import com.apartment.domain.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class User extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @Column(name = "full_name")
    private String fullName;

    private String email;

    private String phone;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean active = true;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Column(name = "headline", length = 60)
    private String headline;

    @Column(name = "biography", columnDefinition = "TEXT")
    private String biography;

    @Column(name = "language", length = 10)
    private String language;

    @Column(name = "website", length = 500)
    private String website;

    @Column(name = "facebook", length = 255)
    private String facebook;

    @Column(name = "instagram", length = 255)
    private String instagram;

    @Column(name = "linkedin", length = 500)
    private String linkedin;

    @Column(name = "tiktok", length = 255)
    private String tiktok;

    @Column(name = "github", length = 255)
    private String github;
}