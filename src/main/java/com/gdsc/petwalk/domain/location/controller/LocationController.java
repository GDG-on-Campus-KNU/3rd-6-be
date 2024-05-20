package com.gdsc.petwalk.domain.location.controller;

import com.gdsc.petwalk.domain.location.dto.response.LocationResponseDto;
import com.gdsc.petwalk.domain.location.service.LocationService;
import com.gdsc.petwalk.domain.walkinvitation.entity.WalkInvitation;
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
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping("/nearby")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<LocationResponseDto>> getNearby(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<LocationResponseDto> allWithinRadius =
                locationService.findAllWithinRadius(principalDetails, 1);

        return ResponseEntity.ok().body(allWithinRadius);
    }
}
