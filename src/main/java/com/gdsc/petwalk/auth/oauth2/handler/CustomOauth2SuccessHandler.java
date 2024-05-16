package com.gdsc.petwalk.auth.oauth2.handler;

import com.gdsc.petwalk.auth.jwt.service.JwtService;
import com.gdsc.petwalk.domain.member.service.MemberService;
import com.gdsc.petwalk.global.principal.PrincipalDetails;
import com.gdsc.petwalk.global.redis.service.RedisService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomOauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final RedisService redisService;
    private final MemberService memberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        PrincipalDetails oauth2User = (PrincipalDetails) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = oauth2User.getAuthorities();

        String email = oauth2User.getEmail();
        String role = authorities.iterator().next().getAuthority();

        String accessToken = jwtService.createAccessToken(email, role);
        String refreshToken =  jwtService.createRefreshToken();
        String savedRefresh = memberService.saveRefresh(email, refreshToken);

        // redisService.setRefreshToken(email, refreshToken);

        response.setHeader("Authorization", accessToken);
        response.setHeader("Authorization-refresh", refreshToken);
        // response.sendRedirect("http://localhost:3000");

        log.info("OAuth2 로그인에 성공하였습니다. 이메일 : {}",  oauth2User.getEmail());
        log.info("OAuth2 로그인에 성공하였습니다. Access Token : {}",  accessToken);
        log.info("OAuth2 로그인에 성공하였습니다. Refresh Token : {}",  refreshToken);
        log.info("DB에 저장된 Refresh Token : {}",  savedRefresh);
        // log.info("Redis에 저장된 RefreshToken : {}", redisService.getRefreshToken(email));
    }
}
