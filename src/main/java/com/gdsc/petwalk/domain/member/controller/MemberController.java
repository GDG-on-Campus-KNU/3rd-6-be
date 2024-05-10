package com.gdsc.petwalk.domain.member.controller;

import com.gdsc.petwalk.domain.member.entity.Member;
import com.gdsc.petwalk.domain.member.service.MemberService;
import com.gdsc.petwalk.domain.pet.dto.response.PetResponseDto;
import com.gdsc.petwalk.domain.pet.entity.Pet;
import com.gdsc.petwalk.global.principal.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;


}
