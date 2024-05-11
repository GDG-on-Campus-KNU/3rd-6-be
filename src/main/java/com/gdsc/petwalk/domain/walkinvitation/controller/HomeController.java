package com.gdsc.petwalk.domain.walkinvitation.controller;

import com.gdsc.petwalk.domain.walkinvitation.dto.request.WalkInvitaionCreateRequestDto;
import com.gdsc.petwalk.domain.walkinvitation.dto.response.HomePageResponseDto;
import com.gdsc.petwalk.domain.walkinvitation.dto.response.WalkInvitationDetailsResponseDto;
import com.gdsc.petwalk.domain.walkinvitation.service.WalkInvitationService;
import com.gdsc.petwalk.global.principal.PrincipalDetails;
import lombok.RequiredArgsConstructor;
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
public class HomeController {

    private final WalkInvitationService walkInvitationService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> createWalkInvitation(
            @RequestPart("walkInvitaionCreateRequestDto") WalkInvitaionCreateRequestDto request,
            @RequestPart("uploadPhotos") MultipartFile[] multipartFiles,
            @AuthenticationPrincipal PrincipalDetails principalDetails){

        Long savedId = walkInvitationService.createWalkInvitation(request, multipartFiles, principalDetails);

        return ResponseEntity.ok().body(savedId);
    }

    @GetMapping("/{walkInvitationId}")
    public ResponseEntity<WalkInvitationDetailsResponseDto> getDetail(
            @PathVariable("walkInvitationId") Long id
    ){
        WalkInvitationDetailsResponseDto response = walkInvitationService.getHomeDetailsById(id);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<HomePageResponseDto>> getHomepageLists(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        List<HomePageResponseDto> homepageLists
                = walkInvitationService.getTodayHomePageLists(principalDetails);

        return ResponseEntity.ok().body(homepageLists);
    }
}
