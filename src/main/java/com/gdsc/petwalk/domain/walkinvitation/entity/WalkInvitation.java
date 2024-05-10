package com.gdsc.petwalk.domain.walk.entity;

import com.gdsc.petwalk.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "walk_invitations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WalkInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private double latitude; // 산책 위치 위도

    @Column(nullable = false)
    private double longitude; // 산책 위치 경도

    @Column(nullable = false)
    private String detailedLocation; // 세부 위치

    @Column(nullable = false)
    private LocalDateTime walkDate; // 산책 날짜

    @Column(nullable = false)
    private LocalDateTime walkTime; // 산책 시간

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member writer; // 게시글 작성자
}
