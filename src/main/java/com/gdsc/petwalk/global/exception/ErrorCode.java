package com.gdsc.petwalk.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //401 JWT 인증 관련 오류
    EXPIRED_ACCESS_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED,
            "Access token이 만료되었습니다. Refresh token을 사용하세요."),

    EXPIRED_REFRESH_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED,
            "Refresh token이 만료되었습니다. 다시 로그인하세요."),

    NOT_VALID_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED,
            "유효하지 않은 JWT 토큰입니다."),

    UNSUPPORTED_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED,
            "지원되지 않는 JWT 토큰입니다."),

    MISMATCH_CLAIMS_EXCEPTION(HttpStatus.UNAUTHORIZED,
            "JWT 토큰의 클레임이 일치하지 않거나 토큰이 없습니다."),

    // 409 Member 회원가입 이메일 중복 관련 오류
    MEMBER_ALREADY_EXISTS_EMAIL(HttpStatus.CONFLICT,
            "Member의 Email이 중복됩니다."),

    // 400 Member 회원가입 이메일 형식이 잘못 됨
    MEMBER_NOT_VALID_EMAIL(HttpStatus.BAD_REQUEST,
            "Member의 Email 형식이 잘못 되었습니다");

    private final HttpStatus httpStatus;
    private final String message;
}
