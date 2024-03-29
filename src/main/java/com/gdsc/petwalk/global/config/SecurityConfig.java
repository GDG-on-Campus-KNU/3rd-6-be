package com.gdsc.petwalk.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdsc.petwalk.auth.itself.filter.CustomJsonUserPasswordAuthenticationFilter;
import com.gdsc.petwalk.auth.itself.handler.LoginFailureHandler;
import com.gdsc.petwalk.auth.itself.handler.LoginSuccessHandler;
import com.gdsc.petwalk.auth.itself.service.LoginService;
import com.gdsc.petwalk.auth.jwt.filter.JwtAuthorizationFilter;
import com.gdsc.petwalk.auth.jwt.service.JwtService;
import com.gdsc.petwalk.auth.oauth2.handler.CustomOauth2SuccessHandler;
import com.gdsc.petwalk.auth.oauth2.service.CustomOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtService jwtService;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final CustomOauth2UserService customOauth2UserService;
    private final CustomOauth2SuccessHandler customOauth2SuccessHandler;
    private final PasswordEncoder passwordEncoder;
    private final LoginService loginService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return
                http
                        .cors(AbstractHttpConfigurer::disable)
                        .csrf(AbstractHttpConfigurer::disable)
                        .formLogin(AbstractHttpConfigurer::disable)
                        .httpBasic(AbstractHttpConfigurer::disable)
                        .authorizeHttpRequests((auth) -> auth
                                .requestMatchers(new AntPathRequestMatcher("/error")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/login")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/auth/**")).permitAll()
                                .anyRequest().authenticated()
                        )
                        .oauth2Login((oauth2) -> oauth2
                                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOauth2UserService))
                                .successHandler(customOauth2SuccessHandler))
                        .addFilterAfter(customJsonUserPasswordAuthenticationFilter(), LogoutFilter.class)
                        .addFilterBefore(jwtAuthorizationFilter, CustomJsonUserPasswordAuthenticationFilter.class)
                        .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .build();
    }
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider
                = new DaoAuthenticationProvider(passwordEncoder);

        provider.setUserDetailsService(loginService);

        return new ProviderManager(provider);
    }
    @Bean
    public CustomJsonUserPasswordAuthenticationFilter customJsonUserPasswordAuthenticationFilter() {
        CustomJsonUserPasswordAuthenticationFilter customJsonUserPasswordAuthenticationFilter
                = new CustomJsonUserPasswordAuthenticationFilter(objectMapper());

        customJsonUserPasswordAuthenticationFilter.setAuthenticationManager(authenticationManager());
        customJsonUserPasswordAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        customJsonUserPasswordAuthenticationFilter.setAuthenticationFailureHandler(loginFailureHandler());

        return customJsonUserPasswordAuthenticationFilter;
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(jwtService);
    }

    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
