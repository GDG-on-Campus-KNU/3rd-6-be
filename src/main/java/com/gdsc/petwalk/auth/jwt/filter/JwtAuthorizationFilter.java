package com.gdsc.petwalk.auth.jwt.filter;

import com.gdsc.petwalk.auth.jwt.service.JwtService;
import com.gdsc.petwalk.domain.member.entity.Member;
import com.gdsc.petwalk.domain.member.service.MemberService;
import com.gdsc.petwalk.global.exception.ErrorCode;
import com.gdsc.petwalk.global.exception.NotValidTokenException;
import com.gdsc.petwalk.global.principal.PrincipalDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final MemberService memberService;
    private final JwtService jwtService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // filter를 거치고 싶지 않은 path를 여기서 관리함
        String[] excludePathLists = {"/favicon.ico"};

        String path = request.getRequestURI();

        // 해당 경로로 시작하는 uri에 대해서는 filter를 거치지 않음
        if (path.startsWith("/login") || path.startsWith("/oauth2") || path.startsWith("/api")) {
            return true;
        }

        // excludePathLists과 같은 uri로 매칭되면 true를 반환하고 filter를 거치지않음.
        return Arrays.stream(excludePathLists)
                .anyMatch((excludePath) -> excludePath.equals(path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String accesstoken = request.getHeader("Authorization");

        if (jwtService.validateToken(accesstoken)) {
            //JWT 토큰을 파싱해서 member 정보를 가져옴
            String email = jwtService.getEmail(accesstoken);
            Member member = memberService.findMemberByEmail(email);

            // 해당 멤버를 authentication(인증) 해줌
            authentication(request, response, filterChain, member);
            return;
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 유저를 authentication 해주는 메소드
     */
    private static void authentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Member member) throws IOException, ServletException {
        // PrincipalDetails에 유저 정보를 담기
        PrincipalDetails principalDetails = new PrincipalDetails(member);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authenticationToken
                = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

        // 세션에 사용자 등록, 해당 사용자는 스프링 시큐리티에 의해서 인증됨
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
