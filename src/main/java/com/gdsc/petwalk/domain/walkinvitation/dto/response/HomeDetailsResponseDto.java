package com.gdsc.petwalk.domain.walkinvitation.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class HomeDetailsResponseDto{

    private String title;
    private String content;
    private double latitude; // 산책 위치 위도
    private double longitude; // 산책 위치 경도
    private String detailedLocation; // 세부 위치
    private LocalDateTime walkDate; // 산책 날짜
    private LocalDateTime walkTime; // 산책 시간

    private List<String> WalkInvitationPhotoUrls;
    private String memberPhotoUrl;
    private String memberName;

    @Builder
    public HomeDetailsResponseDto(String title, String content, double latitude, double longitude, String detailedLocation, LocalDateTime walkDate, LocalDateTime walkTime, List<String> walkInvitationPhotoUrls, String memberPhotoUrl, String memberName) {
        this.title = title;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.detailedLocation = detailedLocation;
        this.walkDate = walkDate;
        this.walkTime = walkTime;
        WalkInvitationPhotoUrls = walkInvitationPhotoUrls;
        this.memberPhotoUrl = memberPhotoUrl;
        this.memberName = memberName;
    }
}
