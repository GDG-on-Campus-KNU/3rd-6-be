package com.gdsc.petwalk.domain.walkinvitation.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class WalkInvitaionCreateRequestDto {

    private String title;
    private String content;
    private double latitude; // 산책 위치 위도
    private double longitude; // 산책 위치 경도
    private String detailedLocation; // 세부 위치
    private LocalDateTime walkDateTime; // 산책 시작 날짜 및 시간

}
