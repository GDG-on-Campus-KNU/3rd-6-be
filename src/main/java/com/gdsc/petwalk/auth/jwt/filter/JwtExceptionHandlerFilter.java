package com.gdsc.petwalk.auth.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdsc.petwalk.global.exception.custom.NotValidTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (NotValidTokenException e){
            HttpStatus httpStatus = e.getErrorCode().getHttpStatus();
            String message = e.getErrorCode().getMessage();

            Map<String, Object> jsonResponse = new HashMap<>();
            jsonResponse.put("status", httpStatus.value());
            jsonResponse.put("message", message);

            response.setStatus(httpStatus.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            objectMapper.writeValue(response.getWriter(), jsonResponse);
        }
    }
}
