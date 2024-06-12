package com.gdsc.petwalk.domain.pet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PetResultDto<T> {
    private Boolean status;
    private int code;
    private String message;
    private T data;
}
