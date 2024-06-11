package com.gdsc.petwalk.auth.itself.dto.request;

public record ProfileRequestDto(
        String email, String nickName, double latitude, double longitude
) {
}
