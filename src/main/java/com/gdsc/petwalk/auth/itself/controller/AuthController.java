package com.gdsc.petwalk.auth.itself.controller;

import com.gdsc.petwalk.auth.itself.dto.request.LoginRequestDto;
import com.gdsc.petwalk.auth.itself.dto.request.ProfileRequestDto;
import com.gdsc.petwalk.auth.itself.dto.request.SignUpRequestDto;
import com.gdsc.petwalk.auth.itself.dto.response.AuthResultDto;
import com.gdsc.petwalk.auth.itself.dto.response.TokenResponseDto;
import com.gdsc.petwalk.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Slf4j
@Tag(name = "회원가입, 로그인 api")
public class AuthController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    @Operation(summary = "회원가입 로직", description = "회원가입 로직")
    @ApiResponse(responseCode = "200", description = "signUpRequestDto는 application/json 형식, uploadPhoto는 multipart/form-data 형식으로, 두 개를 한꺼번에 form-data 형식으로 보내주면 됨")
    public ResponseEntity<AuthResultDto<?>> signUp(
            @RequestPart("signUpRequestDto") @Valid SignUpRequestDto request,
            @RequestPart("uploadPhoto") MultipartFile multipartFile) {

        String savedEmail = memberService.signUp(request, multipartFile);
        log.info("회원가입 성공 email = {}", request.email());

        return ResponseEntity.ok().body(AuthResultDto.builder()
                .status(true)
                .code(200)
                .message("회원가입에 성공하였습니다")
                .build());
    }

    @PostMapping("/profile")
    @Operation(summary = "회원가입 후 프로필 설정", description = "profileRequestDto는 application/json 형식, uploadPhoto는 multipart/form-data 형식으로, 두 개를 한꺼번에 form-data 형식으로 보내주면 됨")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<AuthResultDto<?>> setProfile(
            @RequestPart("profileRequestDto") ProfileRequestDto profileRequestDto,
            @RequestPart("uploadPhoto") MultipartFile file
    ) {
        memberService.setMemberProfile(profileRequestDto, file);

        return ResponseEntity.ok().body(AuthResultDto.builder()
                .status(true)
                .code(200)
                .message("프로필 설정에 성공하였습니다")
                .build());
    }

    @PostMapping("/login")
    @Operation(summary = "로그인 로직",
            description = "body에 Authorization은 accesstoken, Authorization-refresh는 refreshtoken",
            responses = {@ApiResponse(responseCode = "200", description = "로그인 성공 시 토큰 반환", content = @Content(schema = @Schema(implementation = TokenResponseDto.class)))})
    public ResponseEntity<TokenResponseDto> login(
            @RequestBody LoginRequestDto loginRequestDto) {

        log.info("테스트 용 login. 실제로 거치지 않음");

        TokenResponseDto tokenResponseDto = TokenResponseDto.builder()
                .accessToken("엑세스토큰")
                .refreshToken("리프레시 토큰")
                .build();

        return ResponseEntity.ok().body(tokenResponseDto);
    }
}
