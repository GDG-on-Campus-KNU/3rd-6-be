package com.gdsc.petwalk.domain.walkinvitation.controller;

import com.gdsc.petwalk.auth.itself.dto.request.SignUpRequestDto;
import com.gdsc.petwalk.domain.walkinvitation.dto.request.WalkInvitaionCreateRequestDto;
import com.gdsc.petwalk.domain.walkinvitation.dto.response.HomePageResponseDto;
import com.gdsc.petwalk.domain.walkinvitation.dto.response.WalkInvitationDetailsResponseDto;
import com.gdsc.petwalk.domain.walkinvitation.service.WalkInvitationService;
import com.gdsc.petwalk.global.principal.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
@Tag(name = "홈 화면 관련 api")
public class HomeController {

    private final WalkInvitationService walkInvitationService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "홈 화면 글쓰기 로직", description = "홈 화면 글쓰기 로직, WalkInvitaionCreateRequestDto는 json형식, uploadPhotos는 multipart/form-data")
    @ApiResponse(responseCode = "200", description = "글쓰기 성공 시 Long 타입 id 값 반환")
    public ResponseEntity<Long> createWalkInvitation(
            @RequestPart("walkInvitaionCreateRequestDto") WalkInvitaionCreateRequestDto request,
            @RequestPart("uploadPhotos") MultipartFile[] multipartFiles,
            @AuthenticationPrincipal PrincipalDetails principalDetails){

        Long savedId = walkInvitationService.createWalkInvitation(request, multipartFiles, principalDetails);

        return ResponseEntity.ok().body(savedId);
    }

    @GetMapping("/{walkInvitationId}")
    @Operation(summary = "홈 화면 글 하나 조회 로직", description = "홈 화면 글 하나 조회하는 로직")
    @ApiResponse(responseCode = "200", description = "글쓰기 성공 시 WalkInvitationDetailsResponseDto 타입 dto 반환")
    public ResponseEntity<WalkInvitationDetailsResponseDto> getDetail(
            @PathVariable("walkInvitationId") Long id
    ){
        WalkInvitationDetailsResponseDto response = walkInvitationService.getHomeDetailsById(id);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "홈 화면에 보여질 글들 조회 로직", description = "홈 화면 글들 조회하는 로직이고, 기본은 오늘 하루치만")
    @ApiResponse(responseCode = "200", description = "글쓰기 성공 시 HomePageResponseDto 타입 dto 반환")
    public ResponseEntity<List<HomePageResponseDto>> getHomepageLists(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        List<HomePageResponseDto> homepageLists
                = walkInvitationService.getTodayHomePageLists(principalDetails);

        return ResponseEntity.ok().body(homepageLists);
    }
}
