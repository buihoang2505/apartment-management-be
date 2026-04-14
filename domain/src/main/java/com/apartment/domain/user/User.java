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
}