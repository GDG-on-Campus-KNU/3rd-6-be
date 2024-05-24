package com.gdsc.petwalk.auth.itself.controller;

import com.gdsc.petwalk.auth.itself.dto.request.LoginRequestDto;
import com.gdsc.petwalk.auth.itself.dto.request.SignUpRequestDto;
import com.gdsc.petwalk.auth.itself.dto.response.TokenResponseDto;
import com.gdsc.petwalk.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "회원가입, 로그인 api")
public class AuthController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    @Operation(summary = "회원가입 로직", description = "회원가입 로직")
    @ApiResponse(responseCode = "200", description = "회원가입 성공 시 '회원가입 성공!' 이라는 메시지 전달")
    public ResponseEntity<String> signUp(
            @RequestBody SignUpRequestDto request) {

        Long savedId = memberService.signUp(request);
        log.info("회원가입 성공 id = {}", savedId);

        return ResponseEntity.ok().body("회원가입 성공!");
    }

    @PostMapping("/login")
    @Operation(summary = "로그인 로직",
            description = "body에 Authorization은 accesstoken, Authorization-refresh는 refreshtoken",
            responses = {@ApiResponse(responseCode = "200", description = "로그인 성공 시 토큰 반환", content = @Content(schema = @Schema(implementation = TokenResponseDto.class)))})
    public ResponseEntity<TokenResponseDto> login(
            @RequestBody LoginRequestDto loginRequestDto) {

        log.info("테스트 용 login. 실제로 거치지 않음");

        TokenResponseDto tokenResponseDto = TokenResponseDto.builder()
                .Authorization("엑세스토큰")
                .AuthorizationRefresh("리프레시 토큰")
                .build();

        return ResponseEntity.ok().body(tokenResponseDto);
    }
}
