package com.gdsc.petwalk.domain.walkinvitation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class WalkInvitationResultDto<T> {
    private Boolean status;
    private int code;
    private String message;
    private T data;
}
