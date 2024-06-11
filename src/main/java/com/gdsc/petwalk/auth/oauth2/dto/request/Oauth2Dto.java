package com.gdsc.petwalk.auth.oauth2.dto.request;

import com.gdsc.petwalk.domain.member.entity.Member;
import com.gdsc.petwalk.domain.member.entity.Role;
import lombok.Builder;

@Builder
public record Oauth2Dto(String email, String role) {

    public Member oauth2DtoToMember(Oauth2Dto oauth2Dto) {
        return Member.builder()
                .email(oauth2Dto.email())
                .role(Role.valueOf(oauth2Dto.role()))
                .build();
    }

}
