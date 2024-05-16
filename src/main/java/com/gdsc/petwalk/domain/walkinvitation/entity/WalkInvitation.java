package com.gdsc.petwalk.domain.walkinvitation.entity;

import com.gdsc.petwalk.domain.member.entity.Member;
import com.gdsc.petwalk.domain.photo.entity.Photo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Setter;

@Entity(name = "walk_invitations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WalkInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "walk_invitaion_id")
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
    private LocalDateTime walkDateTime; // 산책 날짜

    private String walkingStatus; // 산책 상태

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member writer; // 게시글 작성자

    @Setter
    @OneToMany(mappedBy = "walkInvitation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photoUrls = new ArrayList<>();

    @Builder
    public WalkInvitation(Long id, String title, String content, double latitude, double longitude, String detailedLocation, LocalDateTime walkDateTime, String walkingStatus, Member writer, List<Photo> photoUrls) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.detailedLocation = detailedLocation;
        this.walkDateTime = walkDateTime;
        this.walkingStatus = walkingStatus;
        this.writer = writer;
        this.photoUrls = photoUrls;
    }
}
