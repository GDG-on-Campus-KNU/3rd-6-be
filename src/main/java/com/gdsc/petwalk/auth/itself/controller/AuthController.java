package com.gdsc.petwalk.auth.itself.controller;

import com.gdsc.petwalk.auth.itself.dto.request.SignUpRequestDto;
import com.gdsc.petwalk.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Slf4j
public class AuthController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    public ResponseEntity<Long> signUp(
            @RequestBody SignUpRequestDto request){

        Long savedId = memberService.signUp(request);
        log.info("로그인 성공 id = {}", savedId);

        return ResponseEntity.ok().body(savedId);
    }
}
