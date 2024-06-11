package com.gdsc.petwalk.auth.itself.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AuthResultDto<T> {
    private Boolean status;
    private int code;
    private String message;
    private T data;
}