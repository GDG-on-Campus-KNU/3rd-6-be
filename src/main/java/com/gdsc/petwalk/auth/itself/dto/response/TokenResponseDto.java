package com.gdsc.petwalk.auth.itself.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TokenResponseDto {
    private String accessToken;
    private String refreshToken;
}
