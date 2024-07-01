package com.gdsc.petwalk.domain.walkinvitation.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class WalkInvitationDetailsResponseDto {

    private String title;
    private String content;
    private double latitude; // 산책 위치 위도
    private double longitude; // 산책 위치 경도
    private String detailedLocation; // 세부 위치
    private String walkDateTime; // 산책 날짜
    private String walkingStatus;

    private List<String> walkInvitationPhotoUrls;

    private Long memberId;
    private String memberPhotoUrl;
    private String memberName;

    @Builder
    public WalkInvitationDetailsResponseDto(String title, String content, double latitude, double longitude, String detailedLocation, String walkDateTime, String walkingStatus, List<String> walkInvitationPhotoUrls, Long memberId, String memberPhotoUrl, String memberName) {
        this.title = title;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.detailedLocation = detailedLocation;
        this.walkDateTime = walkDateTime;
        this.walkingStatus = walkingStatus;
        this.walkInvitationPhotoUrls = walkInvitationPhotoUrls;
        this.memberId = memberId;
        this.memberPhotoUrl = memberPhotoUrl;
        this.memberName = memberName;
    }
}
