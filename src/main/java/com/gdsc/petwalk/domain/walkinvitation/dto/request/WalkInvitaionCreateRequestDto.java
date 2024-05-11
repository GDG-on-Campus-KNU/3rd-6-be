package com.gdsc.petwalk.domain.walkinvitation.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WalkInvitaionCreateRequestDto {

    private String title;
    private String content;
    private double latitude; // 산책 위치 위도
    private double longitude; // 산책 위치 경도
    private String detailedLocation; // 세부 위치
    private LocalDateTime walkDate; // 산책 날짜
    private LocalDateTime walkTime; // 산책 시간


}
