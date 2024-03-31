package com.gdsc.petwalk.global.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final Long REFRESH_TOKEN_EXPIRATION_DAYS = 14L;

    public void setRefreshToken(String email, String refreshToken){
        redisTemplate.opsForValue().set(email, refreshToken, REFRESH_TOKEN_EXPIRATION_DAYS, TimeUnit.DAYS);
    }

    public String getRefreshToken(String email){
        Object refresh = redisTemplate.opsForValue().get(email);
        if(refresh == null){
            return null;
        } else
            return refresh.toString();
    }

    public void updateRefreshToken(String email, String refreshToken){
        redisTemplate.opsForValue().set(email, refreshToken, REFRESH_TOKEN_EXPIRATION_DAYS, TimeUnit.DAYS);
    }

    public void deleteRefreshToken(String email){
        redisTemplate.delete(email);
    }
}
