package com.gdsc.visionguide.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(name = "name")
    private String name; // 사용자 이름

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role; // 사용자의 역할

    @Column(name = "refresh_token")
    private String refresh;

    @Builder
    public Member(Long id, String name, String email, String password, Role role, String refresh) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.refresh = refresh;
    }

    public void updateMember(Member member) {
        if (member.getEmail() != null) {
            this.email = member.getEmail();
        }

        if (member.getName() != null) {
            this.name = member.getName();
        }

        if (member.getRole() != null) {
            this.role = member.getRole();
        }
    }

    public void updateRefreshToken(String refresh) {
        this.refresh = refresh;
    }
}
