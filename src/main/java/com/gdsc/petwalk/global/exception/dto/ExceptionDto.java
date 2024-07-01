package com.gdsc.petwalk.global.exception.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ExceptionDto {

    private boolean status;
    private String message;
    private String errorCode;

    @Builder
    public ExceptionDto(boolean status, String message, String errorCode) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
    }
}
