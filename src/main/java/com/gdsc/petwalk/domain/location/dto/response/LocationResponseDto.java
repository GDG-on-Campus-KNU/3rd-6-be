package com.gdsc.petwalk.domain.location.dto.response;

import com.gdsc.petwalk.domain.walkinvitation.entity.WalkInvitation;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Builder
@Getter
public class LocationResponseDto {
    private Long walkInvitationId;
    private double latitude; // 산책 위치 위도
    private double longitude; // 산책 위치 경도
    private String photoUrl;

    public static List<LocationResponseDto> makeFrom(List<WalkInvitation> walkInvitationList){

        return walkInvitationList.stream().map((walkInvitation) -> {
            return LocationResponseDto.builder()
                    .walkInvitationId(walkInvitation.getId())
                    .latitude(walkInvitation.getLatitude())
                    .longitude(walkInvitation.getLongitude())
                    //.photoUrl(walkInvitation.getPhotoUrls().get(0).getPhotoUrl();
                    .build();
        }).toList();
    }
}
