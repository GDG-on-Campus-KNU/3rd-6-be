package com.gdsc.visionguide.auth.oauth2.dto.request;

import com.gdsc.visionguide.domain.entity.Member;
import com.gdsc.visionguide.domain.entity.Role;
import lombok.Builder;

@Builder
public record Oauth2Dto(String name, String email, String role) {

    public Member oauth2DtoToMember(Oauth2Dto oauth2Dto) {
        return Member.builder()
                .name(oauth2Dto.name())
                .email(oauth2Dto.email())
                .role(Role.valueOf(oauth2Dto.role()))
                .build();
    }

}
