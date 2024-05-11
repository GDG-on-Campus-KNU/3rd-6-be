package com.gdsc.petwalk.auth.itself.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdsc.petwalk.auth.jwt.service.JwtService;
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
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final RedisService redisService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        PrincipalDetails itselfMember = (PrincipalDetails) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = itselfMember.getAuthorities();

        String email = itselfMember.getEmail();
        String role = authorities.iterator().next().getAuthority();

        String accessToken = jwtService.createAccessToken(email, role);
        String refreshToken = jwtService.createRefreshToken();
        redisService.setRefreshToken(email, refreshToken);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("accessToken", accessToken);
        tokenMap.put("refreshToken", refreshToken);

        String jsonResponse = objectMapper.writeValueAsString(tokenMap);

        // JSON 형식의 응답을 설정하고 전송
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
        response.getWriter().close();

        log.info("자체 로그인에 성공하였습니다. 이메일 : {}",  email);
        log.info("자체 로그인에 성공하였습니다. Access Token : {}",  accessToken);
        log.info("자체 로그인에 성공하였습니다. Refresh Token : {}",  refreshToken);
        log.info("Redis에 저장된 RefreshToken : {}", redisService.getRefreshToken(email));
    }
}
