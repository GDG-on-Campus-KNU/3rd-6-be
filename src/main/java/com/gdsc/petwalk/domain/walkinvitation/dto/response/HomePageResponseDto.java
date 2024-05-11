package com.gdsc.petwalk.domain.walkinvitation.dto.response;

import com.gdsc.petwalk.domain.walkinvitation.entity.WalkInvitation;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class HomePageResponseDto {
    private String title;
    private double latitude; // 산책 위치 위도
    private double longitude; // 산책 위치 경도
    private String detailedLocation; // 세부 위치
    private LocalDateTime walkDateTime;
    private String walkingStatus; // 산책 상태

    private String walkInvitationPhotoUrl; // 썸네일

    @Builder
    public HomePageResponseDto(String title, double latitude, double longitude, String detailedLocation, LocalDateTime walkDateTime, String walkingStatus, String walkInvitationPhotoUrl) {
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.detailedLocation = detailedLocation;
        this.walkDateTime = walkDateTime;
        this.walkingStatus = walkingStatus;
        this.walkInvitationPhotoUrl = walkInvitationPhotoUrl;
    }


    public static List<HomePageResponseDto> getListFrom(List<WalkInvitation> walkInvitations) {

        return walkInvitations.stream().map(walkInvitation -> {
            return HomePageResponseDto.builder()
                    .title(walkInvitation.getTitle())
                    .latitude(walkInvitation.getLatitude())
                    .longitude(walkInvitation.getLongitude())
                    .detailedLocation(walkInvitation.getDetailedLocation())
                    .walkDateTime(walkInvitation.getWalkDateTime())
                    .walkingStatus(walkInvitation.getWalkingStatus())
                    // .walkInvitationPhotoUrl(walkInvitation.getPhotoUrls().get(0).getPhotoUrl())
                    .walkInvitationPhotoUrl(null)
                    .build();
        }).toList();
    }
}
