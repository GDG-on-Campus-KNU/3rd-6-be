package com.gdsc.petwalk.global.exception.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ExceptionDto {

    private String message;
    private String errorCode;

    @Builder
    public ExceptionDto(String message, String errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }
}
