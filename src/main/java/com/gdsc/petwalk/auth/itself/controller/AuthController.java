package com.gdsc.petwalk.auth.itself.controller;

import com.gdsc.petwalk.auth.itself.dto.request.LoginRequestDto;
import com.gdsc.petwalk.auth.itself.dto.request.SignUpRequestDto;
import com.gdsc.petwalk.auth.itself.dto.response.AuthResultDto;
import com.gdsc.petwalk.auth.itself.dto.response.TokenResponseDto;
import com.gdsc.petwalk.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/check-duplicate")
    @Operation(summary = "중복 이메일 체크 로직", description = "중복 이메일 체크 로직")
    @ApiResponse(responseCode = "200", description = "회원가입 전에 이메일 체크. 이미 가입 된 이메일이 존재하면 true, 존재하지 않으면 false")
    public ResponseEntity<AuthResultDto<Boolean>> checkDuplicate(
            @RequestParam String email
    ) {
        boolean isExists = memberService.checkDuplicateEmail(email);

        return ResponseEntity.ok().body(AuthResultDto.<Boolean>builder()
                .status(true)
                .code(200)
                .message("이미 가입 된 이메일이 존재하면 true, 존재하지 않으면 false")
                .data(isExists)
                .build()
        );
    }

    @PostMapping(value = "/sign-up", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "회원가입 로직", description = "signUpRequestDto는 application/json 형식, uploadPhoto는 multipart/form-data 형식으로, 두 개를 한꺼번에 form-data 형식으로 보내주면 됨. <br> try it out을 누르면 dto 정보를 확인 할 수 있습니다. swagger에서 직접 테스트는 안되니 참고하세요!")
    @ApiResponse(responseCode = "200", description = "token 반환")
    public ResponseEntity<AuthResultDto<TokenResponseDto>> signUp(
            @RequestPart("signUpRequestDto") @Valid SignUpRequestDto request,
            @RequestPart("uploadPhoto") MultipartFile multipartFile) {

        TokenResponseDto tokenResponseDto = memberService.signUp(request, multipartFile);
        log.info("회원가입 성공 email = {}", request.email());

        return ResponseEntity.ok().body(AuthResultDto.<TokenResponseDto>builder()
                .status(true)
                .code(200)
                .message("회원가입에 성공하였습니다")
                .data(tokenResponseDto)
                .build());
    }

    @PostMapping("/login")
    @Operation(summary = "로그인 로직", description = "로그인 로직")
    @ApiResponse(responseCode = "200", description = "로그인 성공 시 token 반환")
    public ResponseEntity<AuthResultDto<TokenResponseDto>> login(
            @RequestBody LoginRequestDto loginRequestDto) {

        log.info("테스트 용 login. 실제로 거치지 않음");

        TokenResponseDto tokenResponseDto = TokenResponseDto.builder()
                .accessToken("엑세스토큰")
                .refreshToken("리프레시 토큰")
                .build();

        return ResponseEntity.ok().body(AuthResultDto.<TokenResponseDto>builder()
                .status(true)
                .code(200)
                .message("로그인 성공 !")
                .data(tokenResponseDto)
                .build());
    }
}
