package com.gdsc.petwalk.domain.walkinvitation.controller;

import com.gdsc.petwalk.domain.walkinvitation.dto.request.WalkInvitaionCreateRequestDto;
import com.gdsc.petwalk.domain.walkinvitation.dto.response.HomeDetailsResponseDto;
import com.gdsc.petwalk.domain.walkinvitation.service.HomeService;
import com.gdsc.petwalk.global.principal.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> createWalkInvitation(
            @RequestPart("walkInvitaionCreateRequestDto") WalkInvitaionCreateRequestDto request,
            @RequestPart("uploadPhotos") MultipartFile[] multipartFiles,
            @AuthenticationPrincipal PrincipalDetails principalDetails){

        Long savedId = homeService.createWalkInvitation(request, multipartFiles, principalDetails);

        return ResponseEntity.ok().body(savedId);
    }

    @GetMapping("/{walkInvitationId}")
    public ResponseEntity<HomeDetailsResponseDto> getDetail(
            @PathVariable("walkInvitationId") Long id
    ){
        HomeDetailsResponseDto response = homeService.getHomeDetailsById(id);

        return ResponseEntity.ok().body(response);
    }
}
