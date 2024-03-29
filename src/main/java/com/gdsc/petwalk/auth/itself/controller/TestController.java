package com.gdsc.petwalk.auth.itself.controller;

import com.gdsc.petwalk.auth.principal.PrincipalDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @GetMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> test(
            @AuthenticationPrincipal PrincipalDetails principalDetails
            ){

        log.info("인증된 사용자의 email : {}", principalDetails.getEmail());

        return ResponseEntity.ok().body("test 성공");
    }
}
