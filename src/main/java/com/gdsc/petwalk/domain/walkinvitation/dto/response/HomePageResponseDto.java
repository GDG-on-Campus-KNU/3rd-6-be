package com.gdsc.petwalk.domain.walkinvitation.dto.response;

import com.gdsc.petwalk.domain.walkinvitation.entity.WalkingStatus;
import lombok.Builder;
import lombok.Data;

@Data
public class HomePageResponseDto {
    private Long id; // 글 id
    private String title;
    private double latitude; // 산책 위치 위도
    private double longitude; // 산책 위치 경도
    private String detailedLocation; // 세부 위치
    private String walkDateTime;
    private WalkingStatus walkingStatus; // 산책 상태
    private String walkInvitationPhotoUrl; // 썸네일
    private String timeDifference; // 몇분 전 올라온 글인지

    @Builder
    public HomePageResponseDto(Long id, String title, double latitude, double longitude, String detailedLocation, String walkDateTime, WalkingStatus walkingStatus, String walkInvitationPhotoUrl, String timeDifference) {
        this.id = id;
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.detailedLocation = detailedLocation;
        this.walkDateTime = walkDateTime;
        this.walkingStatus = walkingStatus;
        this.walkInvitationPhotoUrl = walkInvitationPhotoUrl;
        this.timeDifference = timeDifference;
    }
}
