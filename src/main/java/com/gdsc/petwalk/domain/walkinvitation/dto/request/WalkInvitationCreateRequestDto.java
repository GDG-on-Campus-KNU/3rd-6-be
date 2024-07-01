package com.gdsc.petwalk.domain.walkinvitation.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WalkInvitationCreateRequestDto {

    private String title;
    private String content;
    private double latitude; // 산책 위치 위도
    private double longitude; // 산책 위치 경도
    private String detailedLocation; // 세부 위치
    private LocalDateTime walkStartDateTime; // 산책 시작 날짜 및 시간
}
